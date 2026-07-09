package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AuthRepository
import com.example.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UploadUiState {
    object Idle : UploadUiState()
    object Submitting : UploadUiState()
    object Success : UploadUiState()
    data class Error(val message: String) : UploadUiState()
}

class UploadViewModel(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UploadUiState>(UploadUiState.Idle)
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    private val _uploadProgress = MutableStateFlow(0)
    val uploadProgress: StateFlow<Int> = _uploadProgress.asStateFlow()

    // Form inputs
    val titleInput = MutableStateFlow("")
    val descriptionInput = MutableStateFlow("")
    val priceInput = MutableStateFlow("")
    val categoryInput = MutableStateFlow("Books") // default

    // Minimum 3 image URLs/Uris. We seed 3 defaults but let users edit them or pick Unsplash presets
    val imageUrls = MutableStateFlow<List<String>>(listOf("", "", ""))

    // Validations
    val titleError = MutableStateFlow<String?>(null)
    val descriptionError = MutableStateFlow<String?>(null)
    val priceError = MutableStateFlow<String?>(null)
    val imagesError = MutableStateFlow<String?>(null)

    fun uploadProduct() {
        if (!validateInputs()) return

        val title = titleInput.value.trim()
        val description = descriptionInput.value.trim()
        val price = priceInput.value.toDoubleOrNull() ?: 0.0
        val category = categoryInput.value
        val images = imageUrls.value.filter { it.isNotBlank() }

        val currentUser = authRepository.currentUser.value
        val sellerId = currentUser?.userId ?: "anonymous"
        val sellerName = currentUser?.name ?: "Guest Seller"

        _uiState.value = UploadUiState.Submitting
        _uploadProgress.value = 0

        viewModelScope.launch {
            productRepository.uploadProduct(
                title = title,
                description = description,
                category = category,
                price = price,
                imageUris = images,
                sellerId = sellerId,
                sellerName = sellerName,
                onProgress = { progress ->
                    _uploadProgress.value = progress
                }
            ).onSuccess {
                _uiState.value = UploadUiState.Success
                clearForm()
            }.onFailure { err ->
                _uiState.value = UploadUiState.Error(err.message ?: "Failed to upload product")
            }
        }
    }

    fun resetState() {
        _uiState.value = UploadUiState.Idle
        _uploadProgress.value = 0
    }

    fun setImageUrl(index: Int, url: String) {
        val current = imageUrls.value.toMutableList()
        if (index in current.indices) {
            current[index] = url
            imageUrls.value = current
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (titleInput.value.trim().isEmpty()) {
            titleError.value = "Title is required"
            isValid = false
        } else {
            titleError.value = null
        }

        if (descriptionInput.value.trim().length < 10) {
            descriptionError.value = "Description must be at least 10 characters"
            isValid = false
        } else {
            descriptionError.value = null
        }

        val price = priceInput.value.trim().toDoubleOrNull()
        if (price == null || price <= 0) {
            priceError.value = "Please enter a valid positive price"
            isValid = false
        } else {
            priceError.value = null
        }

        val filledImages = imageUrls.value.filter { it.isNotBlank() }
        if (filledImages.size < 3) {
            imagesError.value = "You must provide at least 3 image URLs"
            isValid = false
        } else {
            imagesError.value = null
        }

        return isValid
    }

    private fun clearForm() {
        titleInput.value = ""
        descriptionInput.value = ""
        priceInput.value = ""
        categoryInput.value = "Books"
        imageUrls.value = listOf("", "", "")
        titleError.value = null
        descriptionError.value = null
        priceError.value = null
        imagesError.value = null
    }
}
