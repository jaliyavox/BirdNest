package com.example.boardingbookingapp.ui.screens.listings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.data.model.RoomType
import com.example.boardingbookingapp.data.model.GenderPolicy
import com.example.boardingbookingapp.util.formatCurrency
import com.example.boardingbookingapp.util.formatDistance
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*

@Composable
fun ListingDetailScreen(
    listingId: String,
    isStudentSignedIn: Boolean,
    onContactOwner: (String) -> Unit,
    onSignInRequired: () -> Unit,
    onBack: () -> Unit,
) {
    val listing = DETAIL_LISTINGS[listingId] ?: DETAIL_LISTINGS.values.first()
    var bookmarked by remember { mutableStateOf(false) }

    ModernBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                // Hero image section (Minimal)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(ModernBlueSoft),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(listing.title.take(1), color = ModernPrimary.copy(0.2f), fontSize = 120.sp, fontWeight = FontWeight.ExtraBold)

                    // Top bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ModernTextPrimary, modifier = Modifier.size(20.dp))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            IconButton(
                                onClick = {},
                                modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                            ) {
                                Icon(Icons.Default.Share, null, tint = ModernTextPrimary, modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = { bookmarked = !bookmarked },
                                modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                            ) {
                                Icon(
                                    if (bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    null, tint = if (bookmarked) ModernPrimary else ModernTextPrimary,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .offset(y = (-30).dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(ModernCard)
                        .padding(24.dp)
                ) {
                    // Title row
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(listing.title, color = ModernTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 30.sp)
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.LocationOn, null, tint = ModernPrimary, modifier = Modifier.size(16.dp))
                                Text(listing.address, color = ModernTextSecondary, fontSize = 14.sp)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Price and Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(listing.pricePerMonth.formatCurrency(), color = ModernPrimary, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                            Text("per month", color = ModernTextTertiary, fontSize = 14.sp)
                        }
                        if (listing.reviewCount > 0) {
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(ModernBlueSoft).padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.Star, null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                                    Text(String.format("%.1f", listing.averageRating), color = ModernPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    // Quick info
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        item { InfoChip("Type", listing.roomType.name.replace("_", " ")) }
                        item { InfoChip("Gender", listing.genderPolicy.name) }
                        item { InfoChip("Distance", listing.distanceFromCampusKm.formatDistance()) }
                    }

                    Spacer(Modifier.height(32.dp))

                    // Description
                    Text("About This Place", color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Text(listing.description, color = ModernTextSecondary, fontSize = 15.sp, lineHeight = 24.sp)

                    Spacer(Modifier.height(32.dp))

                    // Amenities
                    if (listing.amenities.isNotEmpty()) {
                        Text("Amenities", color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        listing.amenities.chunked(2).forEach { pair ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                                pair.forEach { amenity ->
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    ) {
                                        Box(
                                            modifier = Modifier.size(24.dp).clip(CircleShape).background(ModernBlueSoft),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Icon(Icons.Default.Check, null, tint = ModernPrimary, modifier = Modifier.size(14.dp))
                                        }
                                        Text(amenity, color = ModernTextSecondary, fontSize = 14.sp)
                                    }
                                }
                                if (pair.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                        Spacer(Modifier.height(32.dp))
                    }

                    // Owner section
                    Text("Listed by", color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    ModernCard(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(ModernPrimary),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(listing.ownerName.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(listing.ownerName, color = ModernTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Verified Owner", color = SuccessGreen, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                            Icon(Icons.AutoMirrored.Filled.Chat, null, tint = ModernPrimary, modifier = Modifier.size(24.dp))
                        }
                    }

                    Spacer(Modifier.height(120.dp))
                }
            }

            // Bottom action bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(ModernCard)
                    .navigationBarsPadding()
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (!isStudentSignedIn) {
                        Text(
                            "Sign in as a student to book this place.",
                            color = ModernTextSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ModernButton(
                        text = if (isStudentSignedIn) "Contact Owner" else "Sign in to Book",
                        onClick = {
                            if (isStudentSignedIn) onContactOwner(listing.ownerId)
                            else onSignInRequired()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Column {
        Text(label, color = ModernTextTertiary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(ModernBackground)
                .padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Text(value, color = ModernTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private val DETAIL_LISTINGS = mapOf(
    "1" to Listing(id="1", ownerId="o1", ownerName="Nimali Fernando", title="Modern Furnished Room — Malabe", description="Fully furnished, safe neighborhood 5 min from SLIIT. WiFi and hot water included. Private bathroom available.", pricePerMonth=18000, roomType=RoomType.SINGLE, genderPolicy=GenderPolicy.GIRLS, amenities=listOf("WiFi","AC","Hot Water","Parking","Laundry","Security"), address="No. 45, Malabe Rd, Malabe", distanceFromCampusKm=0.5, isVerified=true, averageRating=4.5f, reviewCount=12),
    "2" to Listing(id="2", ownerId="o2", ownerName="Ruwan Silva", title="Shared Annex — 2 Rooms", description="Clean annex with shared kitchen and bathroom. Walking distance to SLIIT. Common area with TV.", pricePerMonth=12000, roomType=RoomType.ANNEX, genderPolicy=GenderPolicy.BOYS, amenities=listOf("WiFi","Kitchen","Security","Fan"), address="123/A Pitipana Rd, Homagama", distanceFromCampusKm=1.2, isVerified=true, averageRating=4.2f, reviewCount=8),
    "3" to Listing(id="3", ownerId="o3", ownerName="Kamani Perera", title="Cozy Single Room Near Bus Stop", description="Quiet room in family home. Home-cooked meals optional at extra cost. Very family-friendly environment.", pricePerMonth=14000, roomType=RoomType.SINGLE, genderPolicy=GenderPolicy.MIXED, amenities=listOf("WiFi","Meals","Hot Water","Laundry"), address="78/B, Kaduwela Rd, Malabe", distanceFromCampusKm=0.8, isVerified=true, averageRating=4.7f, reviewCount=21),
    "4" to Listing(id="4", ownerId="o4", ownerName="Ishara Jayawardena", title="Whole House for 4 Students", description="3-bedroom house with garden, garage and security cameras. Perfect for a group of students.", pricePerMonth=55000, roomType=RoomType.WHOLE_HOUSE, genderPolicy=GenderPolicy.MIXED, amenities=listOf("WiFi","AC","Parking","Garden","Security","Laundry","Hot Water"), address="24, Narahenpita Ave, Malabe", distanceFromCampusKm=1.8, isVerified=false),
    "5" to Listing(id="5", ownerId="o2", ownerName="Ruwan Silva", title="Shared Room — Girls Only", description="Share with one other girl. Clean, safe, close to shuttle route. Very friendly housemates.", pricePerMonth=9500, roomType=RoomType.SHARED, genderPolicy=GenderPolicy.GIRLS, amenities=listOf("WiFi","Hot Water","Laundry","Fan"), address="56/3, Athurugiriya Rd, Malabe", distanceFromCampusKm=0.6, isVerified=true, averageRating=4.0f, reviewCount=5),
    "6" to Listing(id="6", ownerId="o5", ownerName="Saman Kumara", title="Budget Room Near SLIIT Gate", description="Affordable option with basic amenities. 2 min walk from campus. Ideal for first-year students.", pricePerMonth=8000, roomType=RoomType.SINGLE, genderPolicy=GenderPolicy.BOYS, amenities=listOf("WiFi","Fan"), address="12, New Kandy Rd, Malabe", distanceFromCampusKm=0.2, isVerified=true, averageRating=3.8f, reviewCount=4),
)
