package com.example.primerapractica.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerapractica.domain.usecase.auth.GetCurrentUidUseCase
import com.example.primerapractica.domain.usecase.auth.LoginUserUseCase
import com.example.primerapractica.domain.usecase.auth.LogoutUseCase
import com.example.primerapractica.domain.usecase.auth.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class AuthEvent {
    data object NavigateHome : AuthEvent()
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUidUseCase: GetCurrentUidUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    fun isLoggedIn(): Boolean = getCurrentUidUseCase() != null
    fun uidOrNull(): String? = getCurrentUidUseCase()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                loginUserUseCase(email.trim(), password)
                _state.value = AuthState.Idle
                _events.emit(AuthEvent.NavigateHome)
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                registerUserUseCase(email.trim(), password)
                _state.value = AuthState.Idle
                _events.emit(AuthEvent.NavigateHome)
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Error al registrarse")
            }
        }
    }

    fun logout() {
        logoutUseCase()
        _state.value = AuthState.Idle
    }

    fun clearError() {
        if (_state.value is AuthState.Error) _state.value = AuthState.Idle
    }
}