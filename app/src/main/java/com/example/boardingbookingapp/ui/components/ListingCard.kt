package com.example.boardingbookingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.ui.theme.*
import com.example.boardingbookingapp.util.formatCurrency
import com.example.boardingbookingapp.util.formatDistance

@Composable
fun ListingCard(
    listing: Listing,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModernCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column {
            // Content
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                // Image/Icon placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ModernBlueSoft),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(listing.title.take(1), color = ModernPrimary, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = listing.pricePerMonth.formatCurrency() + "/mo",
                            color = ModernPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                        if (listing.averageRating > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Star, null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                                Text(String.format("%.1f", listing.averageRating), color = ModernTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(listing.title, color = ModernTextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)

                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.LocationOn, null, tint = ModernTextTertiary, modifier = Modifier.size(14.dp))
                        Text(listing.address, color = ModernTextSecondary, fontSize = 13.sp, maxLines = 1)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TagChip(listing.roomType.name.replace("_", " "))
                TagChip(listing.genderPolicy.name)
                TagChip(listing.distanceFromCampusKm.formatDistance())
                if (listing.isVerified) {
                    TagChip("Verified", SuccessGreen)
                }
            }
        }
    }
}

@Composable
private fun TagChip(label: String, color: Color = ModernTextSecondary) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .clip(shape)
            .background(ModernBackground)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}
