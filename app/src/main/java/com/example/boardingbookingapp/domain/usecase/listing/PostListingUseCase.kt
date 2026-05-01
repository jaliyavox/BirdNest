package com.example.birdnest.domain.usecase.listing

import com.example.birdnest.data.model.Listing
import com.example.birdnest.data.repository.ListingRepository
import com.example.birdnest.util.Result

// Sprint 3 — inject via Hilt
class PostListingUseCase(private val listingRepository: ListingRepository) {
    suspend operator fun invoke(listing: Listing, imageUris: List<String>): Result<Listing> =
        listingRepository.postListing(listing, imageUris)
}
