package com.example.boardingbookingapp.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardingbookingapp.data.model.PlatformReview
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

    init {
        // Real-time listener: pending first, then approved, newest-first within each group
        firestore.collection("platform_reviews")
            .addSnapshotListener { snap, _ ->
                _reviews.value = snap?.documents
                    ?.mapNotNull { it.toObject(PlatformReview::class.java) }
                    ?.sortedWith(compareBy({ it.isApproved }, { -it.createdAt }))
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
}
