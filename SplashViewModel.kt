package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class SplashEvent {
    object NavigateToLogin : SplashEvent()
    object NavigateToHome : SplashEvent()
}

class SplashViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<SplashEvent>()
    val navigationEvent: SharedFlow<SplashEvent> = _navigationEvent.asSharedFlow()

    init {
        startSplashTimer()
    }

    private fun startSplashTimer() {
        viewModelScope.launch {
            delay(1500) // Beautiful 1.5-second brand presence delay
            if (authRepository.isUserLoggedIn) {
                _navigationEvent.emit(SplashEvent.NavigateToHome)
            } else {
                _navigationEvent.emit(SplashEvent.NavigateToLogin)
            }
        }
    }
}
