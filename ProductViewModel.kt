package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.FakeStoreProduct
import com.example.data.model.FavoriteEntity
import com.example.data.model.Product
import com.example.data.repository.AuthRepository
import com.example.data.repository.FavoriteRepository
import com.example.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class RecommendedUiState {
    object Loading : RecommendedUiState()
    data class Success(val products: List<FakeStoreProduct>) : RecommendedUiState()
    data class Error(val message: String) : RecommendedUiState()
}

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // Filter and Search States
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    // Recommended Products UiState
    private val _recommendedUiState = MutableStateFlow<RecommendedUiState>(RecommendedUiState.Loading)
    val recommendedUiState: StateFlow<RecommendedUiState> = _recommendedUiState.asStateFlow()

    // Combine Products with Search & Filter
    val productsState: StateFlow<List<Product>> = combine(
        productRepository.allProducts,
        _searchQuery,
        _selectedCategory
    ) { products, query, category ->
        products.filter { product ->
            val matchesQuery = query.isBlank() ||
                    product.title.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true) ||
                    product.sellerName.contains(query, ignoreCase = true)
            
            val matchesCategory = category == "All" || 
                    product.category.equals(category, ignoreCase = true)

            matchesQuery && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Last Viewed cached from Room
    val lastViewedProducts = productRepository.lastViewedProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Current details item selected
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    init {
        fetchRecommended()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
        viewModelScope.launch {
            productRepository.addToLastViewed(product)
        }
    }

    fun fetchRecommended() {
        _recommendedUiState.value = RecommendedUiState.Loading
        viewModelScope.launch {
            productRepository.getRecommendedProducts()
                .onSuccess { list ->
                    _recommendedUiState.value = RecommendedUiState.Success(list)
                }
                .onFailure { err ->
                    _recommendedUiState.value = RecommendedUiState.Error(err.message ?: "Could not load recommendations")
                }
        }
    }

    // Toggle Favorite Action (inserts or removes from database)
    fun toggleFavorite(product: Product, isCurrentlyFav: Boolean) {
        viewModelScope.launch {
            if (isCurrentlyFav) {
                favoriteRepository.removeFavorite(product.productId)
            } else {
                val entity = FavoriteEntity(
                    productId = product.productId,
                    title = product.title,
                    price = product.price,
                    imageUrl = product.imageUrls.firstOrNull() ?: ""
                )
                favoriteRepository.addFavorite(entity)
            }
        }
    }

    fun toggleFakeStoreFavorite(fakeStoreProduct: FakeStoreProduct, isCurrentlyFav: Boolean) {
        viewModelScope.launch {
            val productId = "fake_${fakeStoreProduct.id}"
            if (isCurrentlyFav) {
                favoriteRepository.removeFavorite(productId)
            } else {
                val entity = FavoriteEntity(
                    productId = productId,
                    title = fakeStoreProduct.title,
                    price = fakeStoreProduct.price,
                    imageUrl = fakeStoreProduct.image
                )
                favoriteRepository.addFavorite(entity)
            }
        }
    }

    fun isFavorite(productId: String): StateFlow<Boolean> {
        val result = MutableStateFlow(false)
        viewModelScope.launch {
            favoriteRepository.isFavorite(productId).collect {
                result.value = it
            }
        }
        return result.asStateFlow()
    }
}
