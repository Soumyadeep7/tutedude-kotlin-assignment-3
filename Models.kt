package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val profileImage: String = ""
)

data class Product(
    val productId: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val categoryId: Int = 1,
    val type: String = "Sell", // "Sell" or "Rent"
    val condition: String = "Used", // "New", "Like New", "Used"
    val rentPrice: Double = 0.0,
    val rentAvailable: Boolean = false,
    val sellerId: String = "",
    val sellerName: String = "",
    val sellerCollege: String = "",
    val location: String = "",
    val rating: Double = 5.0,
    val isAvailable: Boolean = true,
    val imageUrls: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
) {
    // Empty constructor for Firestore serialization
    constructor() : this(
        "", "", "", 0.0, "", 1, "Sell", "Used", 0.0, false,
        "", "", "", "", 5.0, true, emptyList(), System.currentTimeMillis()
    )
}

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val productId: String,
    val title: String,
    val price: Double,
    val imageUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class FakeStoreProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: FakeStoreRating
)

data class FakeStoreRating(
    val rate: Double,
    val count: Int
)
