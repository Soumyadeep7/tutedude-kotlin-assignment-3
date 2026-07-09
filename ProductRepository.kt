package com.example.data.repository

import com.example.data.model.FakeStoreProduct
import com.example.data.model.Product
import com.example.data.local.LastViewedEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    val allProducts: Flow<List<Product>>
    val lastViewedProducts: Flow<List<LastViewedEntity>>

    suspend fun uploadProduct(
        title: String,
        description: String,
        category: String,
        price: Double,
        imageUris: List<String>,
        sellerId: String,
        sellerName: String,
        onProgress: (Int) -> Unit
    ): Result<Product>

    suspend fun getProductById(productId: String): Product?
    
    suspend fun addToLastViewed(product: Product)
    
    suspend fun getRecommendedProducts(): Result<List<FakeStoreProduct>>

    suspend fun deleteProduct(productId: String): Result<Unit>
}
