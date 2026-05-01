package com.example.birdnest.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdnest.ui.components.*
import com.example.birdnest.ui.theme.*

private data class ConvUi(
    val id: String,
    val name: String,
    val listing: String,
    val lastMessage: String,
    val time: String,
    val unread: Int,
)

private val MOCK_CONVERSATIONS = listOf(
    ConvUi("c1", "Nimali Fernando", "Modern Furnished Room — Malabe", "Is the room still available from next month?", "2m", 2),
    ConvUi("c2", "Ruwan Silva", "Shared Annex — 2 Rooms", "Sure, feel free to visit on Saturday!", "1h", 0),
    ConvUi("c3", "Kamani Perera", "Cozy Single Room Near Bus Stop", "I've uploaded the payment receipt", "3h", 1),
    ConvUi("c4", "Tharaka Bandara", "Roommate Request", "Hey! Saw your profile on roommate finder 👋", "Yesterday", 0),
    ConvUi("c5", "Ishara Jayawardena", "Whole House for 4 Students", "Can we negotiate on the price?", "2d", 0),
)

@Composable
fun ConversationsScreen(
    onOpenChat: (String) -> Unit,
) {
    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
                Text("Messages", color = ModernTextPrimary, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Text("${MOCK_CONVERSATIONS.count { it.unread > 0 }} unread conversations", color = ModernTextSecondary, fontSize = 14.sp)
            }

            if (MOCK_CONVERSATIONS.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ChatBubbleOutline, null, tint = ModernTextTertiary, modifier = Modifier.size(64.dp))
                        Spacer(Modifier.height(16.dp))
                        Text("No conversations yet", color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Contact an owner to start chatting", color = ModernTextSecondary, fontSize = 14.sp)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(MOCK_CONVERSATIONS, key = { it.id }) { conv ->
                        ConversationItem(conv = conv, onClick = { onOpenChat(conv.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationItem(conv: ConvUi, onClick: () -> Unit) {
    val hasUnread = conv.unread > 0

    ModernCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(ModernBlueSoft),
                contentAlignment = Alignment.Center,
            ) {
                Text(conv.name.take(1), color = ModernPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(conv.name, color = ModernTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(conv.time, color = if (hasUnread) ModernPrimary else ModernTextTertiary, fontSize = 12.sp, fontWeight = if (hasUnread) FontWeight.Bold else FontWeight.Normal)
                }
                Spacer(Modifier.height(2.dp))
                Text(conv.listing, color = ModernPrimary, fontSize = 12.sp, maxLines = 1, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = conv.lastMessage,
                    color = if (hasUnread) ModernTextPrimary else ModernTextSecondary,
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontWeight = if (hasUnread) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            if (conv.unread > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(ModernPrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(conv.unread.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
