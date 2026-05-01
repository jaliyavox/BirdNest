package com.example.boardingbookingapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardingbookingapp.data.model.User
import com.example.boardingbookingapp.data.model.UserRole
import com.example.boardingbookingapp.data.auth.UserSession
import com.example.boardingbookingapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _pendingEmail = MutableStateFlow("")
    val pendingEmail: StateFlow<String> = _pendingEmail.asStateFlow()

    fun sendOtp(email: String) {
        if (!authRepository.isEmailValid(email)) {
            _state.value = AuthState.InvalidDomain
            return
        }
        _pendingEmail.value = email
        _state.value = AuthState.Loading
        viewModelScope.launch {
            when (val result = authRepository.sendOtp(email)) {
                is com.example.boardingbookingapp.util.Result.Success -> _state.value = AuthState.OtpSent
                is com.example.boardingbookingapp.util.Result.Error   -> _state.value = AuthState.Error(result.message)
                else -> {}
            }
        }
    }

    fun verifyOtp(otp: String) {
        if (otp.length < 6) {
            _state.value = AuthState.Error("Enter all 6 digits")
            return
        }
        _state.value = AuthState.Loading
        viewModelScope.launch {
            when (val result = authRepository.verifyOtp(_pendingEmail.value, otp)) {
                is com.example.boardingbookingapp.util.Result.Success -> {
                    UserSession.signIn(result.data)
                    _state.value = AuthState.NeedsProfile
                }
                is com.example.boardingbookingapp.util.Result.Error -> _state.value = AuthState.Error(result.message)
                else -> {}
            }
        }
    }

    fun completeStudentProfile(displayName: String) {
        if (displayName.isBlank()) {
            _state.value = AuthState.Error("Name cannot be empty")
            return
        }
        _state.value = AuthState.Loading
        viewModelScope.launch {
            val user = UserSession.currentUser.value?.copy(displayName = displayName)
                ?: return@launch
            when (val result = authRepository.createProfile(user)) {
                is com.example.boardingbookingapp.util.Result.Success -> {
                    UserSession.signIn(result.data)
                    _state.value = AuthState.Authenticated(result.data)
                }
                is com.example.boardingbookingapp.util.Result.Error -> _state.value = AuthState.Error(result.message)
                else -> {}
            }
        }
    }

    fun loginOwner(email: String, password: String) {
        if (email.isBlank() || password.length < 6) {
            _state.value = AuthState.Error("Invalid credentials")
            return
        }
        _state.value = AuthState.Loading
        viewModelScope.launch {
            when (val result = authRepository.signInWithEmail(email, password)) {
                is com.example.boardingbookingapp.util.Result.Success -> {
                    UserSession.signIn(result.data)
                    _state.value = AuthState.Authenticated(result.data)
                }
                is com.example.boardingbookingapp.util.Result.Error -> _state.value = AuthState.Error(result.message)
                else -> {}
            }
        }
    }

    fun registerOwner(name: String, email: String, password: String, phone: String) {
        if (email.isBlank() || password.length < 6) {
            _state.value = AuthState.Error("Fill all fields")
            return
        }
        _state.value = AuthState.Loading
        _pendingEmail.value = email
        viewModelScope.launch {
            when (val result = authRepository.registerOwner(email, password)) {
                is com.example.boardingbookingapp.util.Result.Success -> {
                    val user = result.data.copy(displayName = name, phone = phone)
                    authRepository.createProfile(user)
                    UserSession.signIn(user)
                    _state.value = AuthState.NeedsKyc
                }
                is com.example.boardingbookingapp.util.Result.Error -> _state.value = AuthState.Error(result.message)
                else -> {}
            }
        }
    }

    fun submitKyc() {
        val uid = UserSession.currentUser.value?.id ?: return
        _state.value = AuthState.Loading
        viewModelScope.launch {
            when (val result = authRepository.submitKyc(uid, "", "")) {
                is com.example.boardingbookingapp.util.Result.Success -> _state.value = AuthState.KycPending
                is com.example.boardingbookingapp.util.Result.Error   -> _state.value = AuthState.Error(result.message)
                else -> {}
            }
        }
    }

    fun clearError() { _state.value = AuthState.Idle }
}

sealed class AuthState {
    data object Idle          : AuthState()
    data object Loading       : AuthState()
    data object OtpSent       : AuthState()
    data object NeedsProfile  : AuthState()
    data object NeedsKyc      : AuthState()
    data object KycPending    : AuthState()
    data object InvalidDomain : AuthState()
    data class  Authenticated(val user: User) : AuthState()
    data class  Error(val message: String)    : AuthState()
}
