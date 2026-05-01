package com.example.birdnest.data.model

data class Feedback(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val category: FeedbackCategory = FeedbackCategory.GENERAL,
    val message: String = "",
    val rating: Int = 0,
    val isResolved: Boolean = false,
    val adminReply: String = "",
    val createdAt: Long = 0L,
)

enum class FeedbackCategory { GENERAL, BUG_REPORT, FEATURE_REQUEST, LISTING_COMPLAINT, USER_COMPLAINT }
