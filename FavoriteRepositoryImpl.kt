package com.example.data.repository

import com.example.data.local.FavoriteDao
import com.example.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepositoryImpl(private val favoriteDao: FavoriteDao) : FavoriteRepository {
    override val allFavorites: Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()

    override fun isFavorite(productId: String): Flow<Boolean> = favoriteDao.isFavorite(productId)

    override suspend fun addFavorite(favorite: FavoriteEntity) {
        favoriteDao.insertFavorite(favorite)
    }

    override suspend fun removeFavorite(productId: String) {
        favoriteDao.deleteFavoriteById(productId)
    }
}
