package com.example.birdnest.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.birdnest.ui.theme.BackgroundDeep
import com.example.birdnest.ui.theme.OrbCyan
import com.example.birdnest.ui.theme.OrbViolet

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_orb")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue  = 1.0f,
        animationSpec = infiniteRepeatable(
            animation   = tween(5000, easing = FastOutSlowInEasing),
            repeatMode  = RepeatMode.Reverse,
        ),
        label = "pulse",
    )
    val drift by infiniteTransition.animateFloat(
        initialValue = -0.05f,
        targetValue  = 0.05f,
        animationSpec = infiniteRepeatable(
            animation   = tween(8000, easing = FastOutSlowInEasing),
            repeatMode  = RepeatMode.Reverse,
        ),
        label = "drift",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                // solid dark base
                drawRect(BackgroundDeep)

                // violet orb — top-left, breathes
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            OrbViolet.copy(alpha = 0.55f * pulse),
                            Color.Transparent,
                        ),
                        center = Offset(
                            x = size.width * (0.15f + drift),
                            y = size.height * 0.18f,
                        ),
                        radius = size.width * 0.70f * pulse,
                    ),
                    radius = size.width * 0.70f * pulse,
                    center = Offset(size.width * (0.15f + drift), size.height * 0.18f),
                )

                // cyan orb — bottom-right, slower drift
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            OrbCyan.copy(alpha = 0.30f * pulse),
                            Color.Transparent,
                        ),
                        center = Offset(
                            x = size.width * (0.85f - drift),
                            y = size.height * 0.88f,
                        ),
                        radius = size.width * 0.55f,
                    ),
                    radius = size.width * 0.55f,
                    center = Offset(size.width * (0.85f - drift), size.height * 0.88f),
                )
            },
        content = content,
    )
}
