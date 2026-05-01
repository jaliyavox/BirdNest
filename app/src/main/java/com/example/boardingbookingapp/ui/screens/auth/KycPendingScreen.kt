package com.example.birdnest.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
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
import com.example.birdnest.ui.components.GlassButton
import com.example.birdnest.ui.components.GlassCard
import com.example.birdnest.ui.components.GradientBackground
import com.example.birdnest.ui.theme.SuccessGreen
import com.example.birdnest.ui.theme.TextSecondary
import com.example.birdnest.ui.theme.TextTertiary
import com.example.birdnest.ui.theme.VioletLight
import com.example.birdnest.ui.theme.WarningAmber
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

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(pulse.value)
                    .clip(CircleShape)
                    .background(WarningAmber.copy(alpha = 0.15f))
                    .border(2.dp, WarningAmber.copy(alpha = 0.50f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(44.dp))
            }

            Spacer(Modifier.height(32.dp))
            Text("Verification Pending", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Text(
                text      = "Your documents are under review.\nThis usually takes 24–48 hours.",
                color     = TextSecondary,
                fontSize  = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
            )

            Spacer(Modifier.height(40.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    KycStep(number = "1", label = "Documents Uploaded", done = true)
                    KycStep(number = "2", label = "Admin Review", done = false, active = true)
                    KycStep(number = "3", label = "Account Activated", done = false)
                }
            }

            Spacer(Modifier.height(32.dp))

            if (demoReady) {
                Text("Demo: Skip wait and enter app", color = TextTertiary, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                GlassButton(text = "Continue to App (Demo)", onClick = onApproved)
            } else {
                Text("We'll notify you via email when approved", color = TextTertiary, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun KycStep(number: String, label: String, done: Boolean, active: Boolean = false) {
    val color = when {
        done   -> SuccessGreen
        active -> VioletLight
        else   -> TextTertiary
    }
    Row(
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.20f))
                .border(1.dp, color.copy(alpha = 0.60f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (done) {
                Icon(Icons.Default.CheckCircle, null, tint = color, modifier = Modifier.size(16.dp))
            } else {
                Text(number, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        Text(
            text       = label,
            color      = if (done || active) Color.White else TextTertiary,
            fontSize   = 14.sp,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}
