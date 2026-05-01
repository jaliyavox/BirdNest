package com.example.birdnest.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdnest.ui.components.ModernBackground
import com.example.birdnest.ui.components.ModernTextField
import com.example.birdnest.ui.theme.*

private data class Msg(val text: String, val isMe: Boolean, val time: String)

private val INITIAL_MESSAGES = listOf(
    Msg("Hi! Is the room still available from next month?", isMe = true, "10:32 AM"),
    Msg("Yes, it's available from the 1st! Would you like to visit?", isMe = false, "10:34 AM"),
    Msg("That would be great! What days work for you?", isMe = true, "10:35 AM"),
    Msg("Saturday or Sunday afternoon works for me.", isMe = false, "10:37 AM"),
    Msg("Perfect, Saturday at 3pm would be great for me", isMe = true, "10:38 AM"),
    Msg("Confirmed! 25th, 3pm. I'll send you the exact address 📍", isMe = false, "10:39 AM"),
)

@Composable
fun ChatScreen(
    conversationId: String,
    onBack: () -> Unit,
) {
    val messages = remember { mutableStateListOf(*INITIAL_MESSAGES.toTypedArray()) }
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    fun sendMessage() {
        val text = inputText.trim()
        if (text.isBlank()) return
        messages.add(Msg(text, isMe = true, "Now"))
        inputText = ""
        keyboard?.hide()
    }

    ModernBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ModernTextPrimary)
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ModernPrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("N", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Nimali Fernando", color = ModernTextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text("Modern Furnished Room · Online", color = ModernPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Messages
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(messages) { msg ->
                    MessageBubble(msg)
                }
            }

            // Input bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ModernTextField(
                    value         = inputText,
                    onValueChange = { inputText = it },
                    label         = "",
                    placeholder   = "Type a message...",
                    modifier      = Modifier.weight(1f),
                )

                IconButton(
                    onClick  = { sendMessage() },
                    enabled  = inputText.isNotBlank(),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) ModernPrimary else ModernBackground)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        null,
                        tint = if (inputText.isNotBlank()) Color.White else ModernTextTertiary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(msg: Msg) {
    val shape = RoundedCornerShape(
        topStart = 20.dp, topEnd = 20.dp,
        bottomStart = if (msg.isMe) 20.dp else 4.dp,
        bottomEnd = if (msg.isMe) 4.dp else 20.dp,
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isMe) Arrangement.End else Arrangement.Start,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 300.dp),
            horizontalAlignment = if (msg.isMe) Alignment.End else Alignment.Start,
        ) {
            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(if (msg.isMe) ModernPrimary else Color.White)
                    .then(if (!msg.isMe) Modifier.border(1.dp, ModernTextTertiary.copy(alpha = 0.1f), shape) else Modifier)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Text(
                    text = msg.text,
                    color = if (msg.isMe) Color.White else ModernTextPrimary,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                )
            }
            Text(
                text = msg.time,
                color = ModernTextTertiary,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
