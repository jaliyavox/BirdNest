package com.example.birdnest.ui.screens.admin

import androidx.lifecycle.ViewModel
import com.example.birdnest.data.model.Listing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Sprint 7 — inject ListingRepository + Firestore directly via @HiltViewModel
class AdminViewModel : ViewModel() {

    private val _pendingListings = MutableStateFlow<List<Listing>>(emptyList())
    val pendingListings: StateFlow<List<Listing>> = _pendingListings.asStateFlow()

    fun loadPendingListings() {
        // Sprint 7: query Firestore where isVerified == false
    }

    fun approveListing(listingId: String) {
        // Sprint 7: set isVerified = true in Firestore
    }

    fun rejectListing(listingId: String, reason: String) {
        // Sprint 7: delete listing, notify owner
    }
}
