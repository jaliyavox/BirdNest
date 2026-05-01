package com.example.birdnest.data.remote.dto

import com.example.birdnest.data.model.GenderPolicy
import com.example.birdnest.data.model.Listing
import com.example.birdnest.data.model.RoomType

data class ListingDto(
    val id: String = "",
    val owner_id: String = "",
    val owner_name: String = "",
    val title: String = "",
    val description: String = "",
    val price_per_month: Int = 0,
    val room_type: String = "SINGLE",
    val gender_policy: String = "MIXED",
    val amenities: List<String> = emptyList(),
    val image_urls: List<String> = emptyList(),
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val address: String = "",
    val is_verified: Boolean = false,
    val average_rating: Float = 0f,
    val review_count: Int = 0,
    val created_at: Long = 0L,
)

fun ListingDto.toDomain() = Listing(
    id = id,
    ownerId = owner_id,
    ownerName = owner_name,
    title = title,
    description = description,
    pricePerMonth = price_per_month,
    roomType = runCatching { RoomType.valueOf(room_type) }.getOrDefault(RoomType.SINGLE),
    genderPolicy = runCatching { GenderPolicy.valueOf(gender_policy) }.getOrDefault(GenderPolicy.MIXED),
    amenities = amenities,
    imageUrls = image_urls,
    lat = lat,
    lng = lng,
    address = address,
    isVerified = is_verified,
    averageRating = average_rating,
    reviewCount = review_count,
    createdAt = created_at,
)
