package com.example.boardingbookingapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardingbookingapp.data.model.Gender
import com.example.boardingbookingapp.data.model.User
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

    // ---------- Student registration ----------

    fun registerStudent(
        firstName: String, lastName: String,
        email: String, password: String,
        dateOfBirth: Long, mobileNumber: String,
        gender: Gender, academicYear: Int,
    ) {
        when {
            firstName.isBlank() || lastName.isBlank() ->
                _state.value = AuthState.Error("Name cannot be empty")
            !authRepository.isEmailValid(email) ->
                _state.value = AuthState.InvalidDomain
            !isPasswordStrong(password) ->
                _state.value = AuthState.Error("Password must include uppercase, lowercase, number, and symbol (min 8 chars)")
            else -> {
                _pendingEmail.value = email
                _state.value = AuthState.Loading
                viewModelScope.launch {
                    when (val r = authRepository.registerStudent(
                        email, password, firstName, lastName,
                        dateOfBirth, mobileNumber, gender, academicYear,
                    )) {
                        is com.example.boardingbookingapp.util.Result.Success -> {
                            UserSession.signIn(r.data)
                            _state.value = AuthState.Authenticated(r.data)
                        }
                        is com.example.boardingbookingapp.util.Result.Error -> _state.value = AuthState.Error(r.message)
                        else -> {}
                    }
                }
            }
        }
    }

    // ---------- Student login (OTP) ----------

    fun sendOtp(email: String) {
        if (!authRepository.isEmailValid(email)) {
            _state.value = AuthState.InvalidDomain
            return
        }
        _pendingEmail.value = email
        _state.value = AuthState.Loading
        viewModelScope.launch {
            when (val r = authRepository.sendOtp(email)) {
                is com.example.boardingbookingapp.util.Result.Success -> _state.value = AuthState.OtpSent
                is com.example.boardingbookingapp.util.Result.Error   -> _state.value = AuthState.Error(r.message)
                else -> {}
            }
        }
    }

    fun verifyOtp(otp: String) {
        if (otp.length < 4) {
            _state.value = AuthState.Error("Enter all 4 digits")
            return
        }
        _state.value = AuthState.Loading
        viewModelScope.launch {
            when (val r = authRepository.verifyOtp(_pendingEmail.value, otp)) {
                is com.example.boardingbookingapp.util.Result.Success -> {
                    UserSession.signIn(r.data)
                    _state.value = AuthState.Authenticated(r.data)
                }
                is com.example.boardingbookingapp.util.Result.Error -> _state.value = AuthState.Error(r.message)
                else -> {}
            }
        }
    }

    // ---------- Owner auth ----------

    fun loginOwner(email: String, password: String) {
        if (email.isBlank() || password.length < 6) {
            _state.value = AuthState.Error("Invalid credentials")
            return
        }
        _state.value = AuthState.Loading
        viewModelScope.launch {
            when (val r = authRepository.signInWithEmail(email, password)) {
                is com.example.boardingbookingapp.util.Result.Success -> {
                    UserSession.signIn(r.data)
                    _state.value = AuthState.Authenticated(r.data)
                }
                is com.example.boardingbookingapp.util.Result.Error -> _state.value = AuthState.Error(r.message)
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
            when (val r = authRepository.registerOwner(email, password)) {
                is com.example.boardingbookingapp.util.Result.Success -> {
                    val user = r.data.copy(displayName = name, phone = phone)
                    authRepository.createProfile(user)
                    UserSession.signIn(user)
                    _state.value = AuthState.NeedsKyc
                }
                is com.example.boardingbookingapp.util.Result.Error -> _state.value = AuthState.Error(r.message)
                else -> {}
            }
        }
    }

    fun submitKyc() {
        val uid = UserSession.currentUser.value?.id ?: return
        _state.value = AuthState.Loading
        viewModelScope.launch {
            when (val r = authRepository.submitKyc(uid, "", "")) {
                is com.example.boardingbookingapp.util.Result.Success -> _state.value = AuthState.KycPending
                is com.example.boardingbookingapp.util.Result.Error   -> _state.value = AuthState.Error(r.message)
                else -> {}
            }
        }
    }

    fun clearError() { _state.value = AuthState.Idle }

    // ---------- Helpers ----------

    fun isPasswordStrong(pw: String): Boolean =
        pw.length >= 8 &&
        pw.any { it.isUpperCase() } &&
        pw.any { it.isLowerCase() } &&
        pw.any { it.isDigit() } &&
        pw.any { !it.isLetterOrDigit() }
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
