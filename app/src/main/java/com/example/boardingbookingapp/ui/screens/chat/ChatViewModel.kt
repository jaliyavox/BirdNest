package com.example.birdnest.ui.screens.chat

import androidx.lifecycle.ViewModel
import com.example.birdnest.data.model.ChatMessage
import com.example.birdnest.data.model.Conversation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Sprint 5 — inject GetMessagesUseCase + SendMessageUseCase + ChatRepository via @HiltViewModel
class ChatViewModel : ViewModel() {

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    val draftText = MutableStateFlow("")

    fun loadConversations(userId: String) {
        // Sprint 5: collect chatRepository.getConversations(userId)
    }

    fun loadMessages(conversationId: String) {
        // Sprint 5: collect getMessagesUseCase(conversationId)
    }

    fun sendMessage(conversationId: String, senderId: String) {
        // Sprint 5: sendMessageUseCase(ChatMessage(...))
    }

    fun sendImage(conversationId: String, imageUri: String) {
        // Sprint 5: chatRepository.sendImage(conversationId, imageUri)
    }
}
