package com.example.birdnest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BirdNestColorScheme = lightColorScheme(
    primary           = ModernPrimary,
    onPrimary         = ModernCard,
    secondary         = ModernPrimary,
    background        = ModernBackground,
    surface           = ModernCard,
    onBackground      = ModernTextPrimary,
    onSurface         = ModernTextPrimary,
    outline           = ModernTextTertiary.copy(alpha = 0.2f),
)

@Composable
fun BirdNestTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BirdNestColorScheme,
        typography  = BirdNestTypography,
        content     = content,
    )
}
