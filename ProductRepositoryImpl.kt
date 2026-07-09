package com.example.data.repository

import android.util.Log
import com.example.data.local.LastViewedDao
import com.example.data.local.LastViewedEntity
import com.example.data.model.FakeStoreProduct
import com.example.data.model.Product
import com.example.data.remote.api.FakeStoreService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ProductRepositoryImpl(
    private val lastViewedDao: LastViewedDao,
    private val fakeStoreService: FakeStoreService
) : ProductRepository {
    private val TAG = "ProductRepositoryImpl"

    private val _simulatedProducts = MutableStateFlow<List<Product>>(emptyList())
    override val allProducts: Flow<List<Product>> = _simulatedProducts.asStateFlow()

    override val lastViewedProducts: Flow<List<LastViewedEntity>> = lastViewedDao.getLastViewedProducts()

    init {
        // Seed initial mock products
        seedMockProducts()
        
        // Listen to Firestore changes if available
        try {
            val db = FirebaseFirestore.getInstance()
            db.collection("products")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e(TAG, "Listen failed in Firestore products collection: ${e.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val firestoreProducts = snapshot.documents.mapNotNull { doc ->
                            try {
                                doc.toObject(Product::class.java)?.copy(productId = doc.id)
                            } catch (parseEx: Exception) {
                                Log.e(TAG, "Error parsing firestore product: ${parseEx.message}")
                                null
                            }
                        }
                        
                        // Merge firestore products with our base seed products
                        val seedList = getInitialSeedList()
                        val merged = (firestoreProducts + seedList).distinctBy { it.productId }
                        _simulatedProducts.value = merged
                    }
                }
        } catch (ex: Exception) {
            Log.w(TAG, "Firestore not fully initialized: ${ex.message}. Operating in Offline/Simulated mode.")
        }
    }

    private fun seedMockProducts() {
        _simulatedProducts.value = getInitialSeedList()
    }

    private fun getInitialSeedList(): List<Product> {
        return listOf(
            Product(
                productId = "P001",
                title = "Engineering Mathematics Book",
                description = "Well maintained second-hand engineering mathematics book. Ideal for college curriculum, covering multivariable calculus, linear algebra, and differential equations.",
                price = 350.0,
                category = "Books",
                categoryId = 1,
                type = "Sell",
                condition = "Used",
                rentPrice = 0.0,
                rentAvailable = false,
                sellerId = "UID123",
                sellerName = "Rahul Das",
                sellerCollege = "Jadavpur University",
                location = "Kolkata",
                rating = 4.8,
                isAvailable = true,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500&auto=format&fit=crop&q=80"
                ),
                timestamp = System.currentTimeMillis() - 3600000
            ),
            Product(
                productId = "P002",
                title = "HP Scientific Calculator",
                description = "Perfect condition calculator. Multi-functional, dual power source, allowed in university engineering exams. Available for rent or sale.",
                price = 1200.0,
                category = "Lab Equipment",
                categoryId = 5,
                type = "Rent",
                condition = "Like New",
                rentPrice = 50.0,
                rentAvailable = true,
                sellerId = "UID456",
                sellerName = "Ananya Roy",
                sellerCollege = "NIT Durgapur",
                location = "Durgapur",
                rating = 4.9,
                isAvailable = true,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1574634534894-89d7576c8259?w=500&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1515825838458-f2a94b20105a?w=500&auto=format&fit=crop&q=80"
                ),
                timestamp = System.currentTimeMillis() - 7200000
            ),
            Product(
                productId = "P003",
                title = "JEE Chemistry Study Material (Allen)",
                description = "Complete set of Allen Chemistry booklets for JEE Main & Advanced preparation. Clean pages with no markings. Covers Unit 01 (Mole Concept & Atomic Structure), Unit 03 (General Organic Chemistry), Unit 06 (Reaction Intermediate), Unit 09 (Oxygen & Nitrogen Organic Compounds), Unit 10 (d & f-Block Elements).",
                price = 420.0,
                category = "Books",
                categoryId = 1,
                type = "Sell",
                condition = "Used",
                rentPrice = 0.0,
                rentAvailable = false,
                sellerId = "UID789",
                sellerName = "Soumyadeep Das",
                sellerCollege = "Heritage Institute of Technology",
                location = "Kolkata",
                rating = 4.7,
                isAvailable = true,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=500&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=500&auto=format&fit=crop&q=80"
                ),
                timestamp = System.currentTimeMillis() - 10800000
            ),
            Product(
                productId = "P004",
                title = "Dorm Single Metal Bed with Mattress",
                description = "Sturdy single occupancy iron frame beds. Standard campus dormitory dimensions. Lined up frame, comes with lightweight premium grey foam mattress. Perfect for hostel rooms and student PG accommodations.",
                price = 1800.0,
                category = "Hostel Essentials",
                categoryId = 6,
                type = "Rent",
                condition = "Used",
                rentPrice = 150.0,
                rentAvailable = true,
                sellerId = "UID101",
                sellerName = "Aniket Sen",
                sellerCollege = "IIEST Shibpur",
                location = "Howrah",
                rating = 4.6,
                isAvailable = true,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=500&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1505691938895-1758d7feb511?w=500&auto=format&fit=crop&q=80"
                ),
                timestamp = System.currentTimeMillis() - 14400000
            ),
            Product(
                productId = "P005",
                title = "JEE Mathematics Study Material (Allen)",
                description = "Complete set of Mathematics books for JEE Main and Advanced. Very well-kept booklets. Units included: Unit 02 (Quadratic Equations, Linear Inequilities), Unit 04 (Point, Straight Lines), Unit 05 (Circle), Unit 08 (Complex Number, Induction, 3D Geometry), Unit 10 (Sets, Relations, Logic).",
                price = 450.0,
                category = "Books",
                categoryId = 1,
                type = "Sell",
                condition = "Used",
                rentPrice = 0.0,
                rentAvailable = false,
                sellerId = "UID789",
                sellerName = "Soumyadeep Das",
                sellerCollege = "Heritage Institute of Technology",
                location = "Kolkata",
                rating = 4.8,
                isAvailable = true,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1509228468518-180dd4864904?w=500&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1453733190148-c44698c265a8?w=500&auto=format&fit=crop&q=80"
                ),
                timestamp = System.currentTimeMillis() - 18000000
            ),
            Product(
                productId = "P006",
                title = "JEE Physics Study Material (Allen)",
                description = "Complete Allen Physics syllabus booklets for JEE Main and Advanced preparation. Excellent self-study reference material. Contains Unit 01 (Basic Mathematics in Physics, Vectors, Units), Unit 03 (Capacitors), Unit 07 (Thermal Physics), Unit 08 (Gravitation, Properties of Matter & Fluid Mechanics), Unit 09 (SHM, Modern Physics).",
                price = 450.0,
                category = "Books",
                categoryId = 1,
                type = "Sell",
                condition = "Used",
                rentPrice = 0.0,
                rentAvailable = false,
                sellerId = "UID789",
                sellerName = "Soumyadeep Das",
                sellerCollege = "Heritage Institute of Technology",
                location = "Kolkata",
                rating = 4.9,
                isAvailable = true,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=500&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1506784983877-45594efa4cbe?w=500&auto=format&fit=crop&q=80"
                ),
                timestamp = System.currentTimeMillis() - 21600000
            ),
            Product(
                productId = "P007",
                title = "Modern Study Desk Setup with Chair",
                description = "Beautiful modern ergonomic modular study desk setup. Includes grey, white and blue drawers/shelving cabinets, space-saving desktop surface, and display shelf for decorative collectibles. Paired with a comfortable premium grey mesh ergonomic office chair with adjustable armrests and gas lift.",
                price = 5500.0,
                category = "Furniture",
                categoryId = 3,
                type = "Sell",
                condition = "Like New",
                rentPrice = 350.0,
                rentAvailable = true,
                sellerId = "UID202",
                sellerName = "Priya Sharma",
                sellerCollege = "Jadavpur University",
                location = "Kolkata",
                rating = 4.9,
                isAvailable = true,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=500&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1524758631624-e2822e304c36?w=500&auto=format&fit=crop&q=80"
                ),
                timestamp = System.currentTimeMillis() - 25200000
            ),
            Product(
                productId = "P008",
                title = "AIEEE & JEE Mock Test Papers & Solutions",
                description = "Excellent revision package for engineering aspirants. Includes previous 11 Years AIEEE & JEE Main Test Papers with fully worked out solutions. Detailed explanations for both physics, chemistry, and mathematics questions. Highly valuable resource for mock tests.",
                price = 180.0,
                category = "Notes",
                categoryId = 4,
                type = "Sell",
                condition = "Used",
                rentPrice = 0.0,
                rentAvailable = false,
                sellerId = "UID789",
                sellerName = "Soumyadeep Das",
                sellerCollege = "Heritage Institute of Technology",
                location = "Kolkata",
                rating = 4.5,
                isAvailable = true,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?w=500&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=500&auto=format&fit=crop&q=80"
                ),
                timestamp = System.currentTimeMillis() - 28800000
            )
        )
    }

    override suspend fun uploadProduct(
        title: String,
        description: String,
        category: String,
        price: Double,
        imageUris: List<String>,
        sellerId: String,
        sellerName: String,
        onProgress: (Int) -> Unit
    ): Result<Product> {
        val newId = UUID.randomUUID().toString()
        
        // Simulating upload progress
        onProgress(10)
        Thread.sleep(150)
        onProgress(35)
        Thread.sleep(200)
        onProgress(70)
        Thread.sleep(150)
        onProgress(100)

        // Seed default high-quality images if no user-supplied local image exists
        val uploadImageUrls = if (imageUris.isEmpty() || imageUris.all { it.isBlank() }) {
            listOf(
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500&auto=format&fit=crop&q=80"
            )
        } else {
            imageUris.map { uri ->
                if (uri.startsWith("http")) uri else "https://images.unsplash.com/photo-1511556532299-8f662fc26c06?w=500&auto=format&fit=crop&q=80"
            }
        }

        val mappedCategoryId = when (category) {
            "Books" -> 1
            "Electronics" -> 2
            "Furniture" -> 3
            "Notes" -> 4
            "Lab Equipment" -> 5
            "Hostel Essentials" -> 6
            else -> 1
        }

        val product = Product(
            productId = newId,
            title = title,
            description = description,
            price = price,
            category = category,
            categoryId = mappedCategoryId,
            type = if (category == "Lab Equipment" || category == "Hostel Essentials") "Rent" else "Sell",
            condition = "Used",
            rentPrice = if (category == "Lab Equipment") 50.0 else if (category == "Hostel Essentials") 150.0 else 0.0,
            rentAvailable = (category == "Lab Equipment" || category == "Hostel Essentials" || category == "Furniture"),
            sellerId = sellerId,
            sellerName = sellerName,
            sellerCollege = "Heritage Institute of Technology",
            location = "Kolkata",
            rating = 4.8,
            isAvailable = true,
            imageUrls = uploadImageUrls,
            timestamp = System.currentTimeMillis()
        )

        // Try writing to Firestore
        try {
            val db = FirebaseFirestore.getInstance()
            db.collection("products").document(newId).set(product)
        } catch (ex: Exception) {
            Log.w(TAG, "Could not upload to Firestore: ${ex.message}. Saving locally.")
        }

        // Add to active simulation stream
        val updatedList = listOf(product) + _simulatedProducts.value
        _simulatedProducts.value = updatedList
        return Result.success(product)
    }

    override suspend fun getProductById(productId: String): Product? {
        return _simulatedProducts.value.find { it.productId == productId }
    }

    override suspend fun addToLastViewed(product: Product) {
        val lastViewed = LastViewedEntity(
            productId = product.productId,
            title = product.title,
            price = product.price,
            imageUrl = product.imageUrls.firstOrNull() ?: "",
            viewedAt = System.currentTimeMillis()
        )
        lastViewedDao.insertLastViewed(lastViewed)
    }

    override suspend fun getRecommendedProducts(): Result<List<FakeStoreProduct>> {
        return try {
            val items = fakeStoreService.getProducts()
            Result.success(items)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch FakeStore recommended products: ${e.message}. Using high-quality simulated recommendations.")
            // High-quality backup recommendations to avoid dead end
            val list = listOf(
                FakeStoreProduct(
                    id = 101,
                    title = "Minimalist Leather Backpack",
                    price = 79.99,
                    description = "Ultra sleek, waterproof bag perfect for campus life.",
                    category = "electronics",
                    image = "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500&auto=format&fit=crop&q=80",
                    rating = com.example.data.model.FakeStoreRating(4.5, 120)
                ),
                FakeStoreProduct(
                    id = 102,
                    title = "Bluetooth Mechanical Keyboard",
                    price = 59.50,
                    description = "Tactile typing experience with gorgeous yellow keycaps.",
                    category = "electronics",
                    image = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500&auto=format&fit=crop&q=80",
                    rating = com.example.data.model.FakeStoreRating(4.8, 85)
                ),
                FakeStoreProduct(
                    id = 103,
                    title = "Hydro insulated Flask 32oz",
                    price = 24.0,
                    description = "Keeps beverages ice-cold for 24 hours.",
                    category = "household",
                    image = "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=500&auto=format&fit=crop&q=80",
                    rating = com.example.data.model.FakeStoreRating(4.2, 230)
                )
            )
            Result.success(list)
        }
    }

    override suspend fun deleteProduct(productId: String): Result<Unit> {
        try {
            val db = FirebaseFirestore.getInstance()
            db.collection("products").document(productId).delete()
        } catch (ex: Exception) {
            Log.e(TAG, "Firestore delete product failed: ${ex.message}")
        }
        val list = _simulatedProducts.value.filter { it.productId != productId }
        _simulatedProducts.value = list
        return Result.success(Unit)
    }
}
