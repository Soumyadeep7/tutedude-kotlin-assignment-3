package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.FavoriteEntity
import com.example.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    val favoritesState: StateFlow<List<FavoriteEntity>> = favoriteRepository.allFavorites
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFromFavorites(productId: String) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(productId)
        }
    }
}
