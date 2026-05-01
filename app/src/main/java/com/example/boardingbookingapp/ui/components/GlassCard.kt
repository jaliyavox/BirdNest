package com.example.boardingbookingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.boardingbookingapp.ui.theme.GlassBorder
import com.example.boardingbookingapp.ui.theme.GlassWhite

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    fillAlpha: Float = 0.10f,
    borderAlpha: Float = 0.28f,
    padContent: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.White.copy(alpha = fillAlpha))
            .border(1.dp, Color.White.copy(alpha = borderAlpha), shape)
            .then(if (padContent) Modifier.padding(20.dp) else Modifier),
        content = content,
    )
}

@Composable
fun GlassChip(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) = GlassCard(
    modifier     = modifier,
    cornerRadius = 50.dp,
    fillAlpha    = 0.15f,
    borderAlpha  = 0.20f,
    padContent   = false,
    content      = content,
)
