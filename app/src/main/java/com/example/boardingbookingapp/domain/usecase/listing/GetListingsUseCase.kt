package com.example.birdnest.domain.usecase.listing

import com.example.birdnest.data.model.Listing
import com.example.birdnest.data.repository.ListingRepository
import kotlinx.coroutines.flow.Flow

// Sprint 2 — inject via Hilt
class GetListingsUseCase(private val listingRepository: ListingRepository) {
    operator fun invoke(): Flow<List<Listing>> = listingRepository.getListings()
}
