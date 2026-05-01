package com.example.birdnest.ui.screens.listings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.birdnest.data.model.GenderPolicy
import com.example.birdnest.data.model.RoomType
import com.example.birdnest.ui.components.*
import com.example.birdnest.ui.screens.home.HomeViewModel
import com.example.birdnest.ui.theme.*
import com.example.birdnest.util.formatCurrency

@Composable
fun ListingsScreen(
    onListingClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val listings by viewModel.filteredListings.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val selectedRoomType by viewModel.selectedRoomType.collectAsState()
    val selectedGender by viewModel.selectedGender.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    var maxPriceSlider by remember { mutableFloatStateOf(60000f) }

    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Search Nest", color = ModernTextPrimary, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                IconButton(
                    onClick = { showFilters = !showFilters },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (showFilters) ModernPrimary else Color.White)
                ) {
                    Icon(
                        Icons.Default.Tune,
                        null,
                        tint = if (showFilters) Color.White else ModernTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Search bar
            ModernTextField(
                value         = query,
                onValueChange = { viewModel.onSearchChanged(it) },
                label         = "",
                placeholder   = "Find area, title, or type...",
                modifier      = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                leadingIcon   = {
                    Icon(Icons.Default.Search, null, tint = ModernTextTertiary, modifier = Modifier.size(20.dp))
                },
            )

            Spacer(Modifier.height(16.dp))

            // Filter panel
            if (showFilters) {
                ModernCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Room Type", color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                RoomType.entries.forEach { rt ->
                                    ModernFilterChip(rt.name.replace("_", " "), selectedRoomType == rt) {
                                        viewModel.onRoomTypeSelected(if (selectedRoomType == rt) null else rt)
                                    }
                                }
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Gender Preference", color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GenderPolicy.entries.forEach { gp ->
                                    ModernFilterChip(gp.name, selectedGender == gp) {
                                        viewModel.onGenderSelected(if (selectedGender == gp) null else gp)
                                    }
                                }
                            }
                        }

                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Max Price", color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(maxPriceSlider.toInt().formatCurrency(), color = ModernPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Slider(
                                value = maxPriceSlider,
                                onValueChange = { maxPriceSlider = it },
                                valueRange = 5000f..100000f,
                                colors = SliderDefaults.colors(
                                    thumbColor        = ModernPrimary,
                                    activeTrackColor  = ModernPrimary,
                                    inactiveTrackColor = ModernBlueSoft,
                                ),
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (listings.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 80.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("No matches found", color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(8.dp))
                                Text("Try different filters or search terms", color = ModernTextSecondary, fontSize = 14.sp)
                            }
                        }
                    }
                } else {
                    item {
                        Text("${listings.size} properties found", color = ModernTextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    items(listings, key = { it.id }) { listing ->
                        ListingCard(listing = listing, onClick = { onListingClick(listing.id) })
                    }
                }
            }
        }
    }
}
