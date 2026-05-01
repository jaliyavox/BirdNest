package com.example.boardingbookingapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardingbookingapp.data.model.RoommateProfile

// Sprint 4 — add swipe gestures, match % ring chart, Coil avatar
@Composable
fun RoommateCard(
    profile: RoommateProfile,
    onMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlassCard(modifier = modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Sprint 4: AsyncImage for avatar
                Column(modifier = Modifier.weight(1f)) {
                    Text(profile.displayName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text(profile.faculty, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
                }
                Text(
                    text = "${profile.matchScore}% match",
                    color = Color(0xFF00D4FF),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(profile.bio, color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp, maxLines = 2)
        }
    }
}
