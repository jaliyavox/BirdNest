package com.example.boardingbookingapp.domain.usecase.listing

import com.example.boardingbookingapp.data.model.GenderPolicy
import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.data.model.RoomType

// Sprint 2 — pure function, no injection needed
class FilterListingsUseCase {
    operator fun invoke(
        listings: List<Listing>,
        query: String = "",
        maxPrice: Int? = null,
        roomType: RoomType? = null,
        genderPolicy: GenderPolicy? = null,
        maxDistanceKm: Double? = null,
        amenities: List<String> = emptyList(),
    ): List<Listing> = listings.filter { listing ->
        (query.isBlank() || listing.title.contains(query, ignoreCase = true) ||
                listing.address.contains(query, ignoreCase = true)) &&
                (maxPrice == null || listing.pricePerMonth <= maxPrice) &&
                (roomType == null || listing.roomType == roomType) &&
                (genderPolicy == null || listing.genderPolicy == genderPolicy) &&
                (maxDistanceKm == null || listing.distanceFromCampusKm <= maxDistanceKm) &&
                (amenities.isEmpty() || listing.amenities.containsAll(amenities))
    }
}
