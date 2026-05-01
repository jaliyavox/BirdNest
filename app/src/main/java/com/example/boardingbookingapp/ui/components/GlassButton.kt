package com.example.birdnest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdnest.ui.theme.CyanAccent
import com.example.birdnest.ui.theme.VioletPrimary

private val ButtonGradient = Brush.horizontalGradient(
    colors = listOf(VioletPrimary, Color(0xFF5E35B1), CyanAccent.copy(alpha = 0.85f)),
)

private val ButtonShape = RoundedCornerShape(14.dp)

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(ButtonShape)
            .background(if (enabled) ButtonGradient else Brush.linearGradient(listOf(Color(0x40FFFFFF), Color(0x40FFFFFF))))
            .border(1.dp, Color.White.copy(alpha = if (enabled) 0.25f else 0.10f), ButtonShape)
            .clickable(enabled = enabled && !isLoading, onClick = onClick),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(8.dp),
            )
        } else {
            Text(
                text       = text,
                color      = if (enabled) Color.White else Color.White.copy(alpha = 0.40f),
                fontWeight = FontWeight.SemiBold,
                fontSize   = 15.sp,
                letterSpacing = 0.3.sp,
            )
        }
    }
}

@Composable
fun GlassOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(ButtonShape)
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.35f), ButtonShape)
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        Text(
            text       = text,
            color      = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize   = 15.sp,
        )
    }
}
