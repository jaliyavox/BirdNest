package com.example.boardingbookingapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.data.auth.UserSession
import com.example.boardingbookingapp.data.model.GenderPolicy
import com.example.boardingbookingapp.data.model.RoomType
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*

@Composable
fun HomeScreen(
    onListingClick: (String) -> Unit,
    onRoommateFinderClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val listings by viewModel.filteredListings.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val selectedRoomType by viewModel.selectedRoomType.collectAsState()
    val selectedGender by viewModel.selectedGender.collectAsState()
    val currentUser by UserSession.currentUser.collectAsState()
    val userInitial = currentUser?.firstName?.firstOrNull()?.uppercase()
        ?: currentUser?.displayName?.firstOrNull()?.uppercase()
        ?: "?"

    ModernBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text("Find Your Nest", color = ModernTextPrimary, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp)
                        Text("Near SLIIT Malabe", color = ModernTextSecondary, fontSize = 14.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Box {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = ModernTextPrimary, modifier = Modifier.size(22.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ModernPrimary)
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
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

                Spacer(Modifier.height(24.dp))

                ModernTextField(
                    value         = query,
                    onValueChange = { viewModel.onSearchChanged(it) },
                    label         = "",
                    placeholder   = "Search by area, title...",
                    leadingIcon   = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = ModernTextTertiary, modifier = Modifier.size(20.dp))
                    },
                )

                Spacer(Modifier.height(20.dp))

                // Filter chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 4.dp)
                ) {
                    item {
                        ModernFilterChip(
                            label    = "All",
                            selected = selectedRoomType == null && selectedGender == null,
                            onClick  = { viewModel.onRoomTypeSelected(null); viewModel.onGenderSelected(null) },
                        )
                    }
                    items(RoomType.entries) { rt ->
                        ModernFilterChip(
                            label    = rt.name.replace("_", " "),
                            selected = selectedRoomType == rt,
                            onClick  = { viewModel.onRoomTypeSelected(if (selectedRoomType == rt) null else rt) },
                        )
                    }
                    items(GenderPolicy.entries) { gp ->
                        ModernFilterChip(
                            label    = gp.name,
                            selected = selectedGender == gp,
                            onClick  = { viewModel.onGenderSelected(if (selectedGender == gp) null else gp) },
                        )
                    }
                }
            }

            // Roommate finder promo card
            RoommatePromoCard(onClick = onRoommateFinderClick, modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(Modifier.height(20.dp))

            // Listings
            if (listings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No listings found", color = ModernTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Try adjusting your filters", color = ModernTextSecondary, fontSize = 14.sp)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("${listings.size} Listings", color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(8.dp))
                            Text("found", color = ModernTextSecondary, fontSize = 14.sp)
                        }
                    }
                    items(listings, key = { it.id }) { listing ->
                        ListingCard(
                            listing = listing,
                            onClick = { onListingClick(listing.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoommatePromoCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    ModernCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ModernBlueSoft),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Groups, contentDescription = null, tint = ModernPrimary, modifier = Modifier.size(28.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Roommate Finder", color = ModernTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Find compatible flatmates near SLIIT", color = ModernTextSecondary, fontSize = 13.sp)
            }
            Icon(Icons.Default.Search, contentDescription = null, tint = ModernPrimary, modifier = Modifier.size(20.dp))
        }
    }
}
