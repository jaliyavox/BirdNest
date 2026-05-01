package com.example.boardingbookingapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    onOpenRentTracker: () -> Unit,
    onOpenAdmin: () -> Unit,
    onSubmitFeedback: () -> Unit = {},
    onSubmitTicket: () -> Unit = {},
) {
    ModernBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                // Header profile card
                ModernCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(ModernPrimary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("S", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp)
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Sathira Malshan", color = ModernTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text("sathira@my.sliit.lk", color = ModernTextSecondary, fontSize = 14.sp)
                        Spacer(Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            ModernBadge("STUDENT", ModernPrimary)
                            ModernBadge("VERIFIED", SuccessGreen)
                        }

                        Spacer(Modifier.height(24.dp))

                        // Stats
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            ProfileStat("3", "Saved")
                            Box(modifier = Modifier.size(width = 1.dp, height = 30.dp).background(ModernTextTertiary.copy(alpha = 0.2f)))
                            ProfileStat("12", "Views")
                            Box(modifier = Modifier.size(width = 1.dp, height = 30.dp).background(ModernTextTertiary.copy(alpha = 0.2f)))
                            ProfileStat("5", "Active")
                        }
                    }
                }
            }

            item {
                Text("General Settings", color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ActionItem(
                        icon  = Icons.Default.Bookmark,
                        label = "Saved Listings",
                        sub   = "3 items saved",
                        onClick = {},
                    )
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
                    ActionItem(
                        icon  = Icons.Default.AdminPanelSettings,
                        label = "Admin Panel",
                        sub   = "Application metrics",
                        onClick = onOpenAdmin,
                    )
                    ActionItem(
                        icon  = Icons.Default.Feedback,
                        label = "Submit Feedback",
                        sub   = "Help us improve BirdNest",
                        onClick = onSubmitFeedback,
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
                    onClick = onSignOut,
                    containerColor = Color.White,
                    contentColor = ErrorRed,
                    modifier = Modifier.fillMaxWidth().border(1.dp, ModernTextTertiary.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
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
private fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, color = ModernTextSecondary, fontSize = 13.sp)
    }
}

@Composable
private fun ActionItem(
    icon: ImageVector,
    label: String,
    sub: String,
    onClick: () -> Unit,
) {
    ModernCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
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
