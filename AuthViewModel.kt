package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.User
import com.example.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<User?> = authRepository.currentUser

    // Input States
    val nameInput = MutableStateFlow("")
    val emailInput = MutableStateFlow("")
    val phoneInput = MutableStateFlow("")
    val passwordInput = MutableStateFlow("")

    // Validation States
    val emailError = MutableStateFlow<String?>(null)
    val passwordError = MutableStateFlow<String?>(null)
    val nameError = MutableStateFlow<String?>(null)
    val phoneError = MutableStateFlow<String?>(null)

    fun login() {
        val email = emailInput.value.trim()
        val password = passwordInput.value

        if (!validateLoginInputs(email, password)) return

        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            authRepository.login(email, password)
                .onSuccess { user ->
                    _uiState.value = AuthUiState.Success(user)
                }
                .onFailure { exception ->
                    _uiState.value = AuthUiState.Error(exception.message ?: "Authentication failed")
                }
        }
    }

    fun register() {
        val name = nameInput.value.trim()
        val email = emailInput.value.trim()
        val phone = phoneInput.value.trim()
        val password = passwordInput.value

        if (!validateRegisterInputs(name, email, phone, password)) return

        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            authRepository.register(name, email, phone, password)
                .onSuccess { user ->
                    _uiState.value = AuthUiState.Success(user)
                }
                .onFailure { exception ->
                    _uiState.value = AuthUiState.Error(exception.message ?: "Registration failed")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState.Idle
            clearInputs()
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun validateLoginInputs(email: String, password: String): Boolean {
        var isValid = true
        
        if (email.isEmpty()) {
            emailError.value = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.value = "Enter a valid email address"
            isValid = false
        } else {
            emailError.value = null
        }

        if (password.isEmpty()) {
            passwordError.value = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordError.value = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordError.value = null
        }

        return isValid
    }

    private fun validateRegisterInputs(name: String, email: String, phone: String, password: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            nameError.value = "Name is required"
            isValid = false
        } else {
            nameError.value = null
        }

        if (email.isEmpty()) {
            emailError.value = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.value = "Enter a valid email address"
            isValid = false
        } else {
            emailError.value = null
        }

        if (phone.isEmpty()) {
            phoneError.value = "Phone number is required"
            isValid = false
        } else {
            phoneError.value = null
        }

        if (password.isEmpty()) {
            passwordError.value = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordError.value = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordError.value = null
        }

        return isValid
    }

    fun clearInputs() {
        nameInput.value = ""
        emailInput.value = ""
        phoneInput.value = ""
        passwordInput.value = ""
        nameError.value = null
        emailError.value = null
        phoneError.value = null
        passwordError.value = null
    }
}
