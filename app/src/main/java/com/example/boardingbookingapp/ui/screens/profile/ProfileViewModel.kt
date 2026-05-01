package com.example.birdnest.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.birdnest.data.model.Listing
import com.example.birdnest.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Sprint 7 — inject AuthRepository + ListingRepository via @HiltViewModel
class ProfileViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _myListings = MutableStateFlow<List<Listing>>(emptyList())
    val myListings: StateFlow<List<Listing>> = _myListings.asStateFlow()

    fun loadProfile() {
        // Sprint 7: collect authRepository.currentUser
    }

    fun signOut() {
        // Sprint 7: authRepository.signOut()
    }
}
