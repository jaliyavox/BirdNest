package com.example.boardingbookingapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.data.auth.UserSession
import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.data.model.PlatformReview
import com.example.boardingbookingapp.ui.components.ModernBackground
import com.example.boardingbookingapp.ui.components.ModernButton
import com.example.boardingbookingapp.ui.components.ModernCard
import com.example.boardingbookingapp.ui.theme.*
import com.example.boardingbookingapp.util.formatCurrency

@Composable
fun HomeScreen(
    onListingClick: (String) -> Unit,
    onRoommateFinderClick: () -> Unit,
    onBrowseAll: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val currentUser by UserSession.currentUser.collectAsState()
    val platformReviews by viewModel.platformReviews.collectAsState()
    val userInitial = currentUser?.firstName?.firstOrNull()?.uppercase()
        ?: currentUser?.displayName?.firstOrNull()?.uppercase() ?: "?"
    val firstName = currentUser?.firstName?.ifBlank { currentUser?.displayName } ?: "Student"

    ModernBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            // ── Header ────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text("Find Your Nest", color = ModernTextPrimary, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp)
                        Text("Near SLIIT Malabe", color = ModernTextSecondary, fontSize = 14.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(ModernPrimary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(userInitial, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }

            // ── Search bar (tap → go to Listings) ────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .clickable { onBrowseAll() }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Default.Search, null, tint = ModernTextTertiary, modifier = Modifier.size(20.dp))
                        Text("Search listings, area, type...", color = ModernTextTertiary, fontSize = 15.sp)
                    }
                }
                Spacer(Modifier.height(28.dp))
            }

            // ── Featured Listings ─────────────────────────────────────
            item {
                SectionHeader(title = "Featured Near You", actionLabel = "See All", onAction = onBrowseAll)
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    items(viewModel.featuredListings, key = { it.id }) { listing ->
                        FeaturedListingCard(listing = listing, onClick = { onListingClick(listing.id) })
                    }
                }
                Spacer(Modifier.height(28.dp))
            }

            // ── Top Owners ────────────────────────────────────────────
            item {
                SectionHeader(title = "Top Owners", actionLabel = null, onAction = {})
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(viewModel.topOwners, key = { it.id }) { owner ->
                        OwnerCard(owner = owner)
                    }
                }
                Spacer(Modifier.height(28.dp))
            }

            // ── Platform Reviews ──────────────────────────────────────
            if (platformReviews.isNotEmpty()) {
                item {
                    SectionHeader(title = "What Students Say", actionLabel = null, onAction = {})
                    Spacer(Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        items(platformReviews, key = { it.id }) { review ->
                            ReviewCard(review = review)
                        }
                    }
                    Spacer(Modifier.height(28.dp))
                }
            }

            // ── Roommate finder promo ─────────────────────────────────
            item {
                ModernCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = onRoommateFinderClick,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(ModernBlueSoft),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Default.Groups, null, tint = ModernPrimary, modifier = Modifier.size(28.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Roommate Finder", color = ModernTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Find compatible flatmates near SLIIT", color = ModernTextSecondary, fontSize = 13.sp)
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = ModernPrimary, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // ── Browse all CTA ────────────────────────────────────────
            item {
                ModernButton(
                    text     = "Browse All Listings",
                    onClick  = onBrowseAll,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                )
            }
        }
    }
}

// ── Sub-composables ────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String, actionLabel: String?, onAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        if (actionLabel != null) {
            Text(
                actionLabel,
                color    = ModernPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onAction() },
            )
        }
    }
}

@Composable
private fun FeaturedListingCard(listing: Listing, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .clickable { onClick() },
    ) {
        Column {
            // Image placeholder with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.linearGradient(listOf(ModernPrimary.copy(0.7f), ModernBlueSoft))
                    ),
                contentAlignment = Alignment.TopEnd,
            ) {
                if (listing.isVerified) {
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SuccessGreen.copy(0.9f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text("Verified", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                // Distance badge bottom-left
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.45f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text("${listing.distanceFromCampusKm} km", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(listing.title, color = ModernTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 17.sp)
                Text(listing.address, color = ModernTextSecondary, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(listing.pricePerMonth.formatCurrency() + "/mo", color = ModernPrimary, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    if (listing.averageRating > 0f) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(13.dp))
                            Text(String.format("%.1f", listing.averageRating), color = ModernTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OwnerCard(owner: OwnerSummary) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp),
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(ModernPrimary, ModernBlueSoft))
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                owner.name.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(owner.name.split(" ").firstOrNull() ?: owner.name, color = ModernTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text("${owner.listingCount} listing${if (owner.listingCount > 1) "s" else ""}", color = ModernTextSecondary, fontSize = 11.sp)
        if (owner.avgRating > 0f) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(11.dp))
                Text(String.format("%.1f", owner.avgRating), color = ModernTextSecondary, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun ReviewCard(review: PlatformReview) {
    Box(
        modifier = Modifier
            .width(240.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(review.rating) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                }
                repeat(5 - review.rating) {
                    Icon(Icons.Default.Star, null, tint = ModernTextTertiary.copy(0.3f), modifier = Modifier.size(14.dp))
                }
            }
            Text(review.comment, color = ModernTextPrimary, fontSize = 13.sp, lineHeight = 18.sp, maxLines = 4, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(ModernBlueSoft),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(review.userName.firstOrNull()?.uppercase() ?: "?", color = ModernPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text(review.userName, color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
