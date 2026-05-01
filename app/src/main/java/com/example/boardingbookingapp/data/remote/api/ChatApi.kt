package com.example.boardingbookingapp.data.remote.api

// Sprint 5 — real-time chat is handled via Firestore snapshots, not REST
// This interface is a placeholder for any additional server-side chat operations
interface ChatApi {
    suspend fun createConversation(participantIds: List<String>, listingId: String): String
}
