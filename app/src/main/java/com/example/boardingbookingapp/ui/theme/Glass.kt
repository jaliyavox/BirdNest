package com.example.boardingbookingapp.ui.theme

import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glassBackground(
    cornerRadius: Dp = 20.dp,
    fillAlpha: Float = 0.12f,
    borderAlpha: Float = 0.30f,
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    return this
        .clip(shape)
        .background(Color.White.copy(alpha = fillAlpha))
        .border(
            width = 1.dp,
            color = Color.White.copy(alpha = borderAlpha),
            shape = shape,
        )
}

fun Modifier.glassCard(cornerRadius: Dp = 20.dp): Modifier =
    glassBackground(cornerRadius = cornerRadius, fillAlpha = 0.10f, borderAlpha = 0.25f)

fun Modifier.glassChip(): Modifier =
    glassBackground(cornerRadius = 50.dp, fillAlpha = 0.15f, borderAlpha = 0.20f)
