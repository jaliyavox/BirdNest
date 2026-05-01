package com.example.boardingbookingapp.ui.screens.listings

import androidx.lifecycle.ViewModel
import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Sprint 3 — inject PostListingUseCase via @HiltViewModel
class ListingsViewModel : ViewModel() {

    private val _postState = MutableStateFlow<Result<Listing>?>(null)
    val postState: StateFlow<Result<Listing>?> = _postState.asStateFlow()

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val price = MutableStateFlow("")
    val address = MutableStateFlow("")
    val selectedImageUris = MutableStateFlow<List<String>>(emptyList())

    fun submitListing() {
        // Sprint 3: validate → postListingUseCase(listing, selectedImageUris.value)
    }

    fun deleteListing(id: String) {
        // Sprint 3: implement
    }
}
