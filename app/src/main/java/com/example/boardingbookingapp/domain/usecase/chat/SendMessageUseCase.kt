package com.example.boardingbookingapp.domain.usecase.chat

import com.example.boardingbookingapp.data.model.ChatMessage
import com.example.boardingbookingapp.data.repository.ChatRepository
import com.example.boardingbookingapp.util.Result

// Sprint 5 — inject via Hilt
class SendMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(message: ChatMessage): Result<Unit> =
        chatRepository.sendMessage(message)
}
