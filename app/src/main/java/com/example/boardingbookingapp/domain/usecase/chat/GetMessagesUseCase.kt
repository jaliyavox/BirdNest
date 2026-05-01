package com.example.birdnest.domain.usecase.chat

import com.example.birdnest.data.model.ChatMessage
import com.example.birdnest.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

// Sprint 5 — inject via Hilt
class GetMessagesUseCase(private val chatRepository: ChatRepository) {
    operator fun invoke(conversationId: String): Flow<List<ChatMessage>> =
        chatRepository.getMessages(conversationId)
}
