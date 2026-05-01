package com.example.boardingbookingapp.data.repository

import com.example.boardingbookingapp.data.model.ChatMessage
import com.example.boardingbookingapp.data.model.Conversation
import com.example.boardingbookingapp.util.Result
import kotlinx.coroutines.flow.Flow

// Sprint 5 — implement with Firestore realtime snapshots
interface ChatRepository {
    fun getConversations(userId: String): Flow<List<Conversation>>
    fun getMessages(conversationId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(message: ChatMessage): Result<Unit>
    suspend fun sendImage(conversationId: String, imageUri: String): Result<Unit>
    suspend fun createConversation(participantIds: List<String>, listingId: String): Result<String>
    suspend fun markRead(conversationId: String, userId: String)
}
