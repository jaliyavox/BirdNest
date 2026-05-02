package com.example.boardingbookingapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardingbookingapp.data.auth.UserSession
import com.example.boardingbookingapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            UserSession.signOut()
        }
    }
}
