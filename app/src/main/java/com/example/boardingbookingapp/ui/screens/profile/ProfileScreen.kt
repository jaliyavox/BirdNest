package com.example.boardingbookingapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.data.auth.UserSession
import com.example.boardingbookingapp.data.model.UserRole
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    onOpenRentTracker: () -> Unit = {},
    onPlatformReview: () -> Unit = {},
    onSubmitTicket: () -> Unit = {},
    onMyListings: () -> Unit = {},
    onPostListing: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val user by UserSession.currentUser.collectAsState()
    val isOwner = user?.role == UserRole.OWNER

    val displayName = user?.let {
        it.displayName.ifBlank { "${it.firstName} ${it.lastName}".trim() }
    } ?: if (isOwner) "Owner" else "Student"
    val initial = displayName.firstOrNull()?.uppercase() ?: "?"
    val email = user?.email ?: ""
    val roleLabel = when (user?.role) {
        UserRole.OWNER -> "OWNER"
        UserRole.ADMIN -> "ADMIN"
        else -> "STUDENT"
    }
    val badgeColor = when (user?.role) {
        UserRole.OWNER -> Color(0xFF059669)
        UserRole.ADMIN -> Color(0xFF7C3AED)
        else -> ModernPrimary
    }

    ModernBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                ModernCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(badgeColor),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(initial, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp)
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(displayName, color = ModernTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(email, color = ModernTextSecondary, fontSize = 14.sp)
                        Spacer(Modifier.height(12.dp))

                        ModernBadge(roleLabel, badgeColor)

                        if (!isOwner && user?.academicYear != null && user!!.academicYear > 0) {
                            Spacer(Modifier.height(12.dp))
                            Text("Year ${user!!.academicYear} · ${user!!.gender.name.lowercase().replaceFirstChar { it.uppercase() }}", color = ModernTextSecondary, fontSize = 13.sp)
                        }
                    }
                }
            }

            item {
                Text("Settings", color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (isOwner) {
                        ActionItem(
                            icon  = Icons.Default.Home,
                            label = "My Listings",
                            sub   = "View and manage your properties",
                            onClick = onMyListings,
                        )
                        ActionItem(
                            icon  = Icons.Default.AddHome,
                            label = "Post a Listing",
                            sub   = "Add a new property",
                            onClick = onPostListing,
                        )
                    } else {
                        ActionItem(
                            icon  = Icons.Default.Groups,
                            label = "Roommate Preferences",
                            sub   = "Update criteria",
                            onClick = {},
                        )
                        ActionItem(
                            icon  = Icons.Default.Payment,
                            label = "Rent Tracker",
                            sub   = "Manage payments",
                            onClick = onOpenRentTracker,
                        )
                    }
                    ActionItem(
                        icon  = Icons.Default.Star,
                        label = "Rate BirdNest",
                        sub   = "Share your experience",
                        onClick = onPlatformReview,
                    )
                    ActionItem(
                        icon  = Icons.Default.SupportAgent,
                        label = "Support Ticket",
                        sub   = "Report an issue",
                        onClick = onSubmitTicket,
                    )
                }
            }

            item {
                ModernButton(
                    text = "Log Out",
                    onClick = {
                        viewModel.signOut()
                        onSignOut()
                    },
                    containerColor = Color.White,
                    contentColor = ErrorRed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ModernTextTertiary.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ModernBadge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ActionItem(
    icon: ImageVector,
    label: String,
    sub: String,
    onClick: () -> Unit,
) {
    ModernCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ModernBlueSoft),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, null, tint = ModernPrimary, modifier = Modifier.size(20.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(label, color = ModernTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(sub, color = ModernTextSecondary, fontSize = 13.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = ModernTextTertiary, modifier = Modifier.size(20.dp))
        }
    }
}
