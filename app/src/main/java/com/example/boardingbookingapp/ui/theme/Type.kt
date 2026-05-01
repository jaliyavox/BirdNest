package com.example.birdnest.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Poppins will be added via Google Fonts provider when Firebase is wired in Sprint 1 full pass.
// Using system font with adjusted letter-spacing as stand-in.

val BirdNestTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Bold,     fontSize = 40.sp, letterSpacing = (-1).sp),
    displayMedium = TextStyle(fontWeight = FontWeight.Bold,    fontSize = 32.sp, letterSpacing = (-0.5).sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 26.sp, letterSpacing = 0.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, letterSpacing = 0.sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, letterSpacing = 0.sp),
    titleLarge  = TextStyle(fontWeight = FontWeight.Medium,    fontSize = 16.sp, letterSpacing = 0.15.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium,    fontSize = 14.sp, letterSpacing = 0.1.sp),
    bodyLarge   = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 15.sp, letterSpacing = 0.15.sp),
    bodyMedium  = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 13.sp, letterSpacing = 0.25.sp),
    labelLarge  = TextStyle(fontWeight = FontWeight.Medium,    fontSize = 13.sp, letterSpacing = 0.1.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium,    fontSize = 11.sp, letterSpacing = 0.5.sp),
)
