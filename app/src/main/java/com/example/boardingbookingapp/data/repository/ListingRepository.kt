package com.example.boardingbookingapp.data.repository

import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.data.model.Review
import com.example.boardingbookingapp.util.Result
import kotlinx.coroutines.flow.Flow

// Sprint 2 — implement with Firestore + Room cache
interface ListingRepository {
    fun getListings(): Flow<List<Listing>>
    fun getListing(id: String): Flow<Listing?>
    suspend fun postListing(listing: Listing, imageUris: List<String>): Result<Listing>
    suspend fun updateListing(listing: Listing): Result<Listing>
    suspend fun deleteListing(id: String): Result<Unit>
    suspend fun submitReview(review: Review): Result<Review>
    fun getReviews(listingId: String): Flow<List<Review>>
}
