package com.example.data.repository

import com.example.data.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<User?>
    val isUserLoggedIn: Boolean
    
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, phone: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun updateProfileImage(imageUrl: String): Result<User>
}
