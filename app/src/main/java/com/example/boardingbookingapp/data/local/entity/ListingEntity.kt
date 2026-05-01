package com.example.boardingbookingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val pricePerMonth: Int,
    val roomType: String,
    val genderPolicy: String,
    val amenities: String,      // JSON array
    val imageUrls: String,      // JSON array
    val lat: Double,
    val lng: Double,
    val address: String,
    val isVerified: Boolean,
    val averageRating: Float,
    val createdAt: Long,
)
