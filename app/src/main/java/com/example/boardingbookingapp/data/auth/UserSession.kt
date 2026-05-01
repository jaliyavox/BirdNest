package com.example.boardingbookingapp.data.auth

import com.example.boardingbookingapp.data.model.User
import com.example.boardingbookingapp.data.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserSession {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun signIn(user: User) {
        _currentUser.value = user
    }

    fun signOut() {
        _currentUser.value = null
    }

    val isStudentSignedIn: Boolean
        get() = _currentUser.value?.role == UserRole.STUDENT
}
