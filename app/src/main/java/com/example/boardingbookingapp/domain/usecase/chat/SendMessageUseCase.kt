package com.example.birdnest.domain.usecase.chat

import com.example.birdnest.data.model.ChatMessage
import com.example.birdnest.data.repository.ChatRepository
import com.example.birdnest.util.Result

// Sprint 5 — inject via Hilt
class SendMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(message: ChatMessage): Result<Unit> =
        chatRepository.sendMessage(message)
}
