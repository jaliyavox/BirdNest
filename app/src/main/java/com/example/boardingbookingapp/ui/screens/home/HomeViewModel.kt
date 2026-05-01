package com.example.boardingbookingapp.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.boardingbookingapp.data.model.GenderPolicy
import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.data.model.RoomType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    val searchQuery      = MutableStateFlow("")
    val selectedRoomType = MutableStateFlow<RoomType?>(null)
    val selectedGender   = MutableStateFlow<GenderPolicy?>(null)

    private val _filteredListings = MutableStateFlow(MOCK_LISTINGS)
    val filteredListings: StateFlow<List<Listing>> = _filteredListings.asStateFlow()

    fun onSearchChanged(q: String) {
        searchQuery.value = q
        applyFilters()
    }

    fun onRoomTypeSelected(rt: RoomType?) {
        selectedRoomType.value = rt
        applyFilters()
    }

    fun onGenderSelected(gp: GenderPolicy?) {
        selectedGender.value = gp
        applyFilters()
    }

    private fun applyFilters() {
        val q  = searchQuery.value
        val rt = selectedRoomType.value
        val gp = selectedGender.value
        _filteredListings.value = MOCK_LISTINGS.filter { l ->
            val matchQ  = q.isBlank() || l.title.contains(q, ignoreCase = true) || l.address.contains(q, ignoreCase = true)
            val matchRT = rt == null || l.roomType == rt
            val matchGP = gp == null || l.genderPolicy == gp
            matchQ && matchRT && matchGP
        }
    }
}

internal val MOCK_LISTINGS = listOf(
    Listing(
        id = "1", ownerId = "o1", ownerName = "Nimali Fernando",
        title = "Modern Furnished Room — Malabe",
        description = "Fully furnished, safe neighborhood 5 min from SLIIT. WiFi included.",
        pricePerMonth = 18000, roomType = RoomType.SINGLE, genderPolicy = GenderPolicy.GIRLS,
        amenities = listOf("WiFi", "AC", "Hot Water", "Parking", "Laundry"),
        address = "No. 45, Malabe Rd, Malabe",
        distanceFromCampusKm = 0.5, isVerified = true, averageRating = 4.5f, reviewCount = 12,
    ),
    Listing(
        id = "2", ownerId = "o2", ownerName = "Ruwan Silva",
        title = "Shared Annex — 2 Rooms Available",
        description = "Clean annex with shared kitchen and bathroom. Walking distance to SLIIT.",
        pricePerMonth = 12000, roomType = RoomType.ANNEX, genderPolicy = GenderPolicy.BOYS,
        amenities = listOf("WiFi", "Kitchen", "Security"),
        address = "123/A Pitipana Rd, Homagama",
        distanceFromCampusKm = 1.2, isVerified = true, averageRating = 4.2f, reviewCount = 8,
    ),
    Listing(
        id = "3", ownerId = "o3", ownerName = "Kamani Perera",
        title = "Cozy Single Room Near Bus Stop",
        description = "Quiet room in family home. Home-cooked meals optional.",
        pricePerMonth = 14000, roomType = RoomType.SINGLE, genderPolicy = GenderPolicy.MIXED,
        amenities = listOf("WiFi", "Meals", "Hot Water"),
        address = "78/B, Kaduwela Rd, Malabe",
        distanceFromCampusKm = 0.8, isVerified = true, averageRating = 4.7f, reviewCount = 21,
    ),
    Listing(
        id = "4", ownerId = "o4", ownerName = "Ishara Jayawardena",
        title = "Whole House for 4 Students",
        description = "3-bedroom house with garden, garage and security. Perfect for a group.",
        pricePerMonth = 55000, roomType = RoomType.WHOLE_HOUSE, genderPolicy = GenderPolicy.MIXED,
        amenities = listOf("WiFi", "AC", "Parking", "Garden", "Security", "Laundry"),
        address = "24, Narahenpita Ave, Malabe",
        distanceFromCampusKm = 1.8, isVerified = false,
    ),
    Listing(
        id = "5", ownerId = "o2", ownerName = "Ruwan Silva",
        title = "Shared Room — Girls Only",
        description = "Share with one other girl. Clean, safe, close to shuttle route.",
        pricePerMonth = 9500, roomType = RoomType.SHARED, genderPolicy = GenderPolicy.GIRLS,
        amenities = listOf("WiFi", "Hot Water", "Laundry"),
        address = "56/3, Athurugiriya Rd, Malabe",
        distanceFromCampusKm = 0.6, isVerified = true, averageRating = 4.0f, reviewCount = 5,
    ),
    Listing(
        id = "6", ownerId = "o5", ownerName = "Saman Kumara",
        title = "Budget Room Near SLIIT Gate",
        description = "Affordable option with basic amenities. 2 min walk from campus.",
        pricePerMonth = 8000, roomType = RoomType.SINGLE, genderPolicy = GenderPolicy.BOYS,
        amenities = listOf("WiFi", "Fan"),
        address = "12, New Kandy Rd, Malabe",
        distanceFromCampusKm = 0.2, isVerified = true, averageRating = 3.8f, reviewCount = 4,
    ),
)
