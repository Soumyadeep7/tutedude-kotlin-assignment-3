package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.components.BannerSlider
import com.example.ui.components.CategoryPill
import com.example.ui.components.CustomBottomNavigationBar
import com.example.ui.components.ProductCard
import com.example.ui.components.RecommendedProductCard
import com.example.ui.components.ShimmerLoading
import com.example.ui.viewmodel.ProductViewModel
import com.example.ui.viewmodel.RecommendedUiState

@Composable
fun HomeScreen(
    productViewModel: ProductViewModel,
    onNavigateToDetails: (Product) -> Unit,
    onNavigateToUpload: () -> Unit,
    onNavigateToTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by productViewModel.searchQuery.collectAsState()
    val selectedCategory by productViewModel.selectedCategory.collectAsState()
    val products by productViewModel.productsState.collectAsState()
    val recommendedState by productViewModel.recommendedUiState.collectAsState()
    val currentUser by productViewModel.isFavorite("seed_1").collectAsState() // dummy for test

    val categories = listOf("All", "Books", "Electronics", "Furniture", "Notes", "Lab Equipment", "Hostel Essentials")

    Scaffold(
        bottomBar = {
            CustomBottomNavigationBar(
                currentRoute = com.example.ui.navigation.Routes.HOME,
                onNavigate = onNavigateToTab
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToUpload,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(bottom = 80.dp) // Offset above floating bottom bar
                    .testTag("upload_fab")
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Upload Product",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 90.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Item
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CampusCart",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Local campus deals & trading",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        
                        // Notification / Bell Action
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable { /* Notification action */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Realtime Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { productViewModel.setSearchQuery(it) },
                        placeholder = { Text("Search books, electronics, notes...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search icon",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(100.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_bar")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Banners Slider
                    BannerSlider(modifier = Modifier.padding(bottom = 8.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    // Categories List
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            CategoryPill(
                                category = category,
                                isSelected = selectedCategory == category,
                                onClick = { productViewModel.setSelectedCategory(category) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (selectedCategory == "All") "Recent Products" else "$selectedCategory Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            // Empty state for products
            if (products.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🛒",
                                fontSize = 48.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "No products found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Be the first to list an item in this category!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            } else {
                // Products Grid Items
                items(products) { product ->
                    val isFav by productViewModel.isFavorite(product.productId).collectAsState(initial = false)
                    ProductCard(
                        product = product,
                        isFavorite = isFav,
                        onFavoriteClick = { productViewModel.toggleFavorite(product, isFav) },
                        onClick = {
                            productViewModel.selectProduct(product)
                            onNavigateToDetails(product)
                        }
                    )
                }
            }

            // Recommended Products Header
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Recommended for You",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Curated recommendations via Retrofit APIs",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            // Recommended Products List (Fake Store APIs)
            when (val recState = recommendedState) {
                is RecommendedUiState.Loading -> {
                    items(3, span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .padding(vertical = 4.dp)
                        ) {
                            ShimmerLoading(modifier = Modifier.fillMaxSize())
                        }
                    }
                }
                is RecommendedUiState.Success -> {
                    if (recState.products.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = "No recommendations found.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    } else {
                        items(recState.products, span = { GridItemSpan(2) }) { fakeProduct ->
                            val isFav by productViewModel.isFavorite("fake_${fakeProduct.id}").collectAsState(initial = false)
                            RecommendedProductCard(
                                product = fakeProduct,
                                isFavorite = isFav,
                                onFavoriteClick = { productViewModel.toggleFakeStoreFavorite(fakeProduct, isFav) },
                                onClick = {
                                    // Map to app product to view details
                                    val mappedProduct = Product(
                                        productId = "fake_${fakeProduct.id}",
                                        title = fakeProduct.title,
                                        description = fakeProduct.description,
                                        price = fakeProduct.price,
                                        category = "Recommended",
                                        sellerId = "fakestore",
                                        sellerName = "FakeStore Partner",
                                        imageUrls = listOf(fakeProduct.image),
                                        timestamp = System.currentTimeMillis()
                                    )
                                    productViewModel.selectProduct(mappedProduct)
                                    onNavigateToDetails(mappedProduct)
                                },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
                is RecommendedUiState.Error -> {
                    item(span = { GridItemSpan(2) }) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Error Loading Recommendations",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = recState.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
