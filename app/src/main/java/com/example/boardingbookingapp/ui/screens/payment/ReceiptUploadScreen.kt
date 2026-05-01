package com.example.birdnest.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdnest.ui.components.*
import com.example.birdnest.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReceiptUploadScreen(
    onBack: () -> Unit,
) {
    var notes by remember { mutableStateOf("") }
    var photoAttached by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ModernTextPrimary)
                }
                Text("Upload Receipt", color = ModernTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))

            if (submitted) {
                SubmittedState(onBack = onBack)
            } else {
                UploadForm(
                    photoAttached = photoAttached,
                    notes = notes,
                    isLoading = isLoading,
                    onPhotoTap = { photoAttached = true },
                    onNotesChange = { notes = it },
                    onSubmit = {
                        scope.launch {
                            isLoading = true
                            delay(1500)
                            isLoading = false
                            submitted = true
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun UploadForm(
    photoAttached: Boolean,
    notes: String,
    isLoading: Boolean,
    onPhotoTap: () -> Unit,
    onNotesChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    ModernCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("May 2026 · Rent Payment", color = ModernTextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text("Rs. 18,000", color = ModernPrimary, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        }
    }

    Spacer(Modifier.height(24.dp))

    Text("Attach Receipt Photo", color = ModernTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(12.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (photoAttached) SuccessGreen.copy(alpha = 0.05f) else ModernBlueSoft)
            .border(
                width = 2.dp,
                color = if (photoAttached) SuccessGreen else ModernPrimary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp),
            )
            .clickable(onClick = onPhotoTap),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(
                imageVector = if (photoAttached) Icons.Default.CheckCircle else Icons.Default.AddPhotoAlternate,
                contentDescription = null,
                tint = if (photoAttached) SuccessGreen else ModernPrimary,
                modifier = Modifier.size(40.dp),
            )
            Text(
                text = if (photoAttached) "Receipt attached" else "Tap to attach receipt photo",
                color = if (photoAttached) SuccessGreen else ModernTextSecondary,
                fontSize = 14.sp,
                fontWeight = if (photoAttached) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        }
    }

    Spacer(Modifier.height(24.dp))

    ModernTextField(
        value = notes,
        onValueChange = onNotesChange,
        label = "NOTES (OPTIONAL)",
        placeholder = "e.g. Bank transfer ref: TXN12345",
    )

    Spacer(Modifier.height(32.dp))

    ModernButton(
        text = "Submit for Review",
        onClick = onSubmit,
        isLoading = isLoading,
        enabled = photoAttached,
    )

    Spacer(Modifier.height(12.dp))

    Text(
        text = "The owner will be notified and will confirm your payment within 24 hours.",
        color = ModernTextTertiary,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(Modifier.height(32.dp))
}

@Composable
private fun SubmittedState(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(SuccessGreen.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.Receipt, null, tint = SuccessGreen, modifier = Modifier.size(48.dp))
        }
        Text("Receipt Submitted!", color = ModernTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Your receipt is pending owner review.\nYou'll be notified once it's confirmed.",
            color = ModernTextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
        )
        Spacer(Modifier.height(8.dp))
        ModernButton(text = "Back to Rent Tracker", onClick = onBack)
    }
}
