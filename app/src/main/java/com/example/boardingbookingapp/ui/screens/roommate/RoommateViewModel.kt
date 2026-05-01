package com.example.birdnest.ui.screens.roommate

import androidx.lifecycle.ViewModel
import com.example.birdnest.data.model.RoommateProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Sprint 4 — inject GetRoommateMatchesUseCase + RoommateRepository via @HiltViewModel
class RoommateViewModel : ViewModel() {

    private val _matches = MutableStateFlow<List<RoommateProfile>>(emptyList())
    val matches: StateFlow<List<RoommateProfile>> = _matches.asStateFlow()

    private val _myProfile = MutableStateFlow<RoommateProfile?>(null)
    val myProfile: StateFlow<RoommateProfile?> = _myProfile.asStateFlow()

    fun loadMatches() {
        // Sprint 4: collect getRoommateMatchesUseCase(myProfile, candidates)
    }

    fun saveProfile(profile: RoommateProfile) {
        // Sprint 4: roommateRepository.saveProfile(profile)
    }

    fun toggleVisibility(visible: Boolean) {
        // Sprint 4: roommateRepository.toggleVisibility(userId, visible)
    }
}
