package com.example.boardingbookingapp.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*

@Composable
fun ConversationsScreen(
    onOpenChat: (String) -> Unit,
    viewModel: ConversationsViewModel = hiltViewModel(),
) {
    val conversations by viewModel.conversations.collectAsState()
    val unreadCount = conversations.count { it.unreadCount > 0 }

    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
                Text("Messages", color = ModernTextPrimary, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    if (unreadCount > 0) "$unreadCount unread conversation${if (unreadCount > 1) "s" else ""}"
                    else "No unread messages",
                    color = ModernTextSecondary,
                    fontSize = 14.sp,
                )
            }

            if (conversations.isEmpty()) {
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
                    items(conversations, key = { it.id }) { conv ->
                        ConversationItem(conv = conv, onClick = { onOpenChat(conv.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationItem(conv: ConversationUi, onClick: () -> Unit) {
    val hasUnread = conv.unreadCount > 0

    ModernCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(ModernBlueSoft),
                contentAlignment = Alignment.Center,
            ) {
                Text(conv.otherPartyName.take(1), color = ModernPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(conv.otherPartyName, color = ModernTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(conv.lastMessageTime, color = if (hasUnread) ModernPrimary else ModernTextTertiary, fontSize = 12.sp, fontWeight = if (hasUnread) FontWeight.Bold else FontWeight.Normal)
                }
                Spacer(Modifier.height(2.dp))
                Text(conv.listingTitle, color = ModernPrimary, fontSize = 12.sp, maxLines = 1, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = conv.lastMessage,
                    color = if (hasUnread) ModernTextPrimary else ModernTextSecondary,
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontWeight = if (hasUnread) FontWeight.SemiBold else FontWeight.Normal,
                )
            }

            if (hasUnread) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(ModernPrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(conv.unreadCount.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
