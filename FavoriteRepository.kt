package com.example.data.repository

import com.example.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    val allFavorites: Flow<List<FavoriteEntity>>
    fun isFavorite(productId: String): Flow<Boolean>
    suspend fun addFavorite(favorite: FavoriteEntity)
    suspend fun removeFavorite(productId: String)
}
