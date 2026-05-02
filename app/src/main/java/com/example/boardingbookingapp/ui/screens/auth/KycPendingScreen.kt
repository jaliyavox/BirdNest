package com.example.boardingbookingapp.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun KycPendingScreen(
    onApproved: () -> Unit,
) {
    val pulse = remember { Animatable(0.9f) }
    var demoReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        pulse.animateTo(
            targetValue   = 1.1f,
            animationSpec = infiniteRepeatable(
                animation  = tween(1200),
                repeatMode = RepeatMode.Reverse,
            ),
        )
    }

    LaunchedEffect(Unit) {
        delay(3000)
        demoReady = true
    }

    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(pulse.value)
                    .clip(CircleShape)
                    .background(WarningAmber.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(56.dp))
            }

            Spacer(Modifier.height(40.dp))
            Text("Verification in Progress", color = ModernTextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(12.dp))
            Text(
                text      = "Our team is reviewing your documents.\nThis usually takes about 24 hours.",
                color     = ModernTextSecondary,
                fontSize  = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
            )

            Spacer(Modifier.height(48.dp))

            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    KycStep(number = "1", label = "Documents Uploaded", done = true)
                    KycStep(number = "2", label = "Admin Review", done = false, active = true)
                    KycStep(number = "3", label = "Account Activated", done = false)
                }
            }

            Spacer(Modifier.height(48.dp))

            if (demoReady) {
                ModernButton(text = "Enter App (Demo)", onClick = onApproved)
            } else {
                Text("We'll notify you via email when approved", color = ModernTextTertiary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun KycStep(number: String, label: String, done: Boolean, active: Boolean = false) {
    val color = when {
        done   -> SuccessGreen
        active -> ModernPrimary
        else   -> ModernTextTertiary
    }
    Row(
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center,
        ) {
            if (done) {
                Icon(Icons.Default.CheckCircle, null, tint = color, modifier = Modifier.size(20.dp))
            } else {
                Text(number, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
        Text(
            text       = label,
            color      = if (done || active) ModernTextPrimary else ModernTextSecondary,
            fontSize   = 15.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
        )
    }
}
