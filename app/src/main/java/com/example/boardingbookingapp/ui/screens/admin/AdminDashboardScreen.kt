package com.example.boardingbookingapp.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*

import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

private val TABS = listOf("Listings", "Users", "Feedback", "Support")

@Composable
fun AdminDashboardScreen(
    onBack: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ModernTextPrimary)
                }
                Text("Admin Panel", color = ModernTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            }

            // Stats Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SummaryBox("12", "Pending", WarningAmber, Modifier.weight(1f))
                SummaryBox("3", "Queue", ModernPrimary, Modifier.weight(1f))
                SummaryBox("48", "Users", SuccessGreen, Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            SecondaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor  = Color.White,
                contentColor    = ModernPrimary,
                divider = { HorizontalDivider(color = ModernTextTertiary.copy(alpha = 0.1f)) }
            ) {
                TABS.forEachIndexed { i, title ->
                    Tab(
                        selected = selectedTab == i,
                        onClick  = { selectedTab = i },
                        text = {
                            Text(
                                title,
                                color     = if (selectedTab == i) ModernPrimary else ModernTextSecondary,
                                fontSize  = 13.sp,
                                fontWeight = if (selectedTab == i) FontWeight.Bold else FontWeight.Medium,
                            )
                        },
                    )
                }
            }

            when (selectedTab) {
                0 -> ListingsTab()
                1 -> UsersTab()
                2 -> FeedbackTab()
                3 -> TicketsTab()
            }
        }
    }
}

@Composable
private fun SummaryBox(value: String, label: String, color: Color, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = color, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, color = color.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ListingsTab() {
    val items = listOf(
        Triple("Modern Furnished Room", "Nimali Fernando", false),
        Triple("Whole House for 4 Students", "Ishara Jayawardena", false),
        Triple("Budget Room Near Gate", "Saman Kumara", true),
    )
    LazyColumn(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(items) { (title, owner, approved) ->
            AdminActionCard(
                icon     = Icons.Default.Home,
                iconColor = if (approved) SuccessGreen else WarningAmber,
                title    = title,
                subtitle = "Owner: $owner",
                status    = if (approved) "LIVE" else "PENDING",
                statusColor = if (approved) SuccessGreen else WarningAmber,
                actions  = if (!approved) listOf("Approve" to ModernPrimary, "Reject" to ErrorRed) else listOf("Revoke" to ErrorRed),
            )
        }
    }
}

@Composable
private fun UsersTab() {
    val users = listOf(
        Triple("Nimali Fernando", "owner@example.com", "KYC Pending"),
        Triple("Kasun Perera", "kasun@my.sliit.lk", "Active"),
        Triple("Amaya Jayasinghe", "amaya@my.sliit.lk", "Active"),
        Triple("Bad Actor", "bad@example.com", "Suspended"),
    )
    LazyColumn(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(users) { (name, email, status) ->
            AdminActionCard(
                icon      = Icons.Default.Person,
                iconColor = when (status) {
                    "Active"      -> SuccessGreen
                    "KYC Pending" -> WarningAmber
                    else          -> ErrorRed
                },
                title    = name,
                subtitle = email,
                status    = status.uppercase(),
                statusColor = when (status) {
                    "Active"      -> SuccessGreen
                    "KYC Pending" -> WarningAmber
                    else          -> ErrorRed
                },
                actions  = when (status) {
                    "KYC Pending" -> listOf("Verify" to ModernPrimary, "Deny" to ErrorRed)
                    "Suspended"   -> listOf("Restore" to SuccessGreen)
                    else          -> listOf("Suspend" to ErrorRed)
                },
            )
        }
    }
}

@Composable
private fun FeedbackTab() {
    val feedbacks = listOf(
        Triple("Great app! But needs better search filters.", "Kasun Perera", "Feature Request"),
        Triple("Owner didn't respond for 3 days.", "Amaya Silva", "Complaint"),
        Triple("Found my boarding house within a week!", "Tharaka B.", "Praise"),
    )
    LazyColumn(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(feedbacks) { (msg, user, category) ->
            AdminActionCard(
                icon      = Icons.Default.Feedback,
                iconColor = ModernPrimary,
                title    = user,
                subtitle = msg,
                status    = category.uppercase(),
                statusColor = ModernPrimary,
                actions  = listOf("Reply" to ModernPrimary, "Resolve" to SuccessGreen),
            )
        }
    }
}

@Composable
private fun TicketsTab() {
    val tickets = listOf(
        Triple("Cannot upload profile photo", "Dilini Silva", "OPEN"),
        Triple("Room marked occupied but isn't", "Nuwan R.", "IN PROGRESS"),
    )
    LazyColumn(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(tickets) { (issue, user, status) ->
            AdminActionCard(
                icon      = Icons.Default.SupportAgent,
                iconColor = WarningAmber,
                title    = user,
                subtitle = issue,
                status    = status,
                statusColor = if (status == "OPEN") WarningAmber else ModernPrimary,
                actions  = if (status == "OPEN") listOf("Start" to ModernPrimary) else listOf("Close" to SuccessGreen),
            )
        }
    }
}

@Composable
private fun AdminActionCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    status: String,
    statusColor: Color,
    actions: List<Pair<String, Color>>,
) {
    ModernCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, color = ModernTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(subtitle, color = ModernTextSecondary, fontSize = 13.sp, maxLines = 1)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (actions.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    actions.forEach { (label, color) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color.copy(alpha = 0.1f))
                                .clickable {}
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
