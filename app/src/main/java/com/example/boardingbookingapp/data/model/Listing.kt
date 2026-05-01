package com.example.boardingbookingapp.data.model

data class Listing(
    val id: String = "",
    val ownerId: String = "",
    val ownerName: String = "",
    val title: String = "",
    val description: String = "",
    val pricePerMonth: Int = 0,
    val roomType: RoomType = RoomType.SINGLE,
    val genderPolicy: GenderPolicy = GenderPolicy.MIXED,
    val amenities: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val address: String = "",
    val distanceFromCampusKm: Double = 0.0,
    val isVerified: Boolean = false,
    val averageRating: Float = 0f,
    val reviewCount: Int = 0,
    val createdAt: Long = 0L,
)

enum class RoomType { SINGLE, SHARED, ANNEX, WHOLE_HOUSE }
enum class GenderPolicy { GIRLS, BOYS, MIXED }
