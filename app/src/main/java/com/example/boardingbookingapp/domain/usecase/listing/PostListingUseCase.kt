package com.example.boardingbookingapp.domain.usecase.listing

import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.data.repository.ListingRepository
import com.example.boardingbookingapp.util.Result

// Sprint 3 — inject via Hilt
class PostListingUseCase(private val listingRepository: ListingRepository) {
    suspend operator fun invoke(listing: Listing, imageUris: List<String>): Result<Listing> =
        listingRepository.postListing(listing, imageUris)
}
