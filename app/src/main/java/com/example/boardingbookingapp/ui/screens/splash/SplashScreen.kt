package com.example.birdnest.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdnest.ui.components.ModernBackground
import com.example.birdnest.ui.theme.ModernPrimary
import com.example.birdnest.ui.theme.ModernTextPrimary
import com.example.birdnest.ui.theme.ModernTextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateNext: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(900, easing = FastOutSlowInEasing))
        scale.animateTo(1f, animationSpec = tween(900, easing = FastOutSlowInEasing))
        delay(1400)
        onNavigateNext()
    }

    ModernBackground {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .alpha(alpha.value)
                        .scale(scale.value)
                        .size(100.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(ModernPrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp),
                    )
                }
                Spacer(Modifier.height(32.dp))
                Text(
                    text       = "BirdNest",
                    color      = ModernTextPrimary,
                    fontSize   = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1.5).sp,
                    modifier   = Modifier.alpha(alpha.value),
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text      = "Find Your Nest Near SLIIT",
                    color     = ModernTextSecondary,
                    fontSize  = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier  = Modifier.alpha(alpha.value),
                )
            }
        }
    }
}
