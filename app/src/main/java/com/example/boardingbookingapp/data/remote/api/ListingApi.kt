package com.example.boardingbookingapp.data.remote.api

import com.example.boardingbookingapp.data.remote.dto.ListingDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Stub — the app uses Firestore directly; this interface is kept for any REST calls
// (e.g. Resend OTP via a Firebase Cloud Function)
interface ListingApi {
    @GET("listings")
    suspend fun getListings(): List<ListingDto>

    @GET("listings/{id}")
    suspend fun getListing(@Path("id") id: String): ListingDto

    @POST("listings")
    suspend fun createListing(@Body dto: ListingDto): ListingDto

    @PUT("listings/{id}")
    suspend fun updateListing(@Path("id") id: String, @Body dto: ListingDto): ListingDto

    @DELETE("listings/{id}")
    suspend fun deleteListing(@Path("id") id: String)
}
