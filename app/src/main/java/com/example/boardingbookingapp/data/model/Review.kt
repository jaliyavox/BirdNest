package com.example.birdnest.data.model

data class Review(
    val id: String = "",
    val listingId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val createdAt: Long = 0L,
)
