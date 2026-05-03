package com.example.boardingbookingapp.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.data.model.PlatformReview
import com.example.boardingbookingapp.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    private val _reviews = MutableStateFlow<List<PlatformReview>>(emptyList())
    val reviews: StateFlow<List<PlatformReview>> = _reviews.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _listings = MutableStateFlow<List<Listing>>(emptyList())
    val listings: StateFlow<List<Listing>> = _listings.asStateFlow()

    init {
        firestore.collection("platform_reviews")
            .addSnapshotListener { snap, _ ->
                _reviews.value = snap?.documents
                    ?.mapNotNull { it.toObject(PlatformReview::class.java) }
                    ?.sortedWith(compareBy({ it.isApproved }, { -it.createdAt }))
                    ?: emptyList()
            }

        firestore.collection("users")
            .addSnapshotListener { snap, _ ->
                _users.value = snap?.documents
                    ?.mapNotNull { it.toObject(User::class.java) }
                    ?: emptyList()
            }

        firestore.collection("listings")
            .addSnapshotListener { snap, _ ->
                _listings.value = snap?.documents
                    ?.mapNotNull { it.toObject(Listing::class.java) }
                    ?: emptyList()
            }
    }

    fun approveReview(id: String) {
        viewModelScope.launch {
            runCatching {
                firestore.collection("platform_reviews").document(id)
                    .update("isApproved", true).await()
            }
        }
    }

    fun deleteReview(id: String) {
        viewModelScope.launch {
            runCatching {
                firestore.collection("platform_reviews").document(id).delete().await()
            }
        }
    }

    fun banUser(uid: String, ban: Boolean) {
        viewModelScope.launch {
            runCatching {
                firestore.collection("users").document(uid)
                    .update("isActive", !ban).await()
            }
        }
    }

    fun approveKyc(uid: String) {
        viewModelScope.launch {
            runCatching {
                firestore.collection("users").document(uid)
                    .update(
                        mapOf(
                            "kycStatus" to "APPROVED",
                            "isKycVerified" to true,
                            "kycNotification" to mapOf(
                                "type" to "APPROVED",
                                "reason" to "",
                                "readAt" to null,
                            )
                        )
                    ).await()
            }
        }
    }

    fun rejectKyc(uid: String, reason: String) {
        viewModelScope.launch {
            runCatching {
                firestore.collection("users").document(uid)
                    .update(
                        mapOf(
                            "kycStatus" to "REJECTED",
                            "isKycVerified" to false,
                            "kycRejectionReason" to reason,
                            "kycNotification" to mapOf(
                                "type" to "REJECTED",
                                "reason" to reason,
                                "readAt" to null,
                            )
                        )
                    ).await()
            }
        }
    }

    fun approveListing(id: String) {
        viewModelScope.launch {
            runCatching {
                firestore.collection("listings").document(id)
                    .update("isVerified", true).await()
            }
        }
    }

    fun removeListing(id: String) {
        viewModelScope.launch {
            runCatching {
                firestore.collection("listings").document(id).delete().await()
            }
        }
    }
}
