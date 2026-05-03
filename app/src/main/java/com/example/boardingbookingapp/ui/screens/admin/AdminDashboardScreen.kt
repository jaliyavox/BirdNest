package com.example.boardingbookingapp.ui.screens.admin

import androidx.compose.foundation.background
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.data.model.KycStatus
import com.example.boardingbookingapp.data.model.Listing
import com.example.boardingbookingapp.data.model.PlatformReview
import com.example.boardingbookingapp.data.model.User
import com.example.boardingbookingapp.data.model.UserRole
import com.example.boardingbookingapp.ui.components.ModernBackground
import com.example.boardingbookingapp.ui.components.ModernCard
import com.example.boardingbookingapp.ui.theme.*

private val TABS = listOf("Listings", "Users", "KYC", "Reviews", "Feedback", "Support")

@Composable
fun AdminDashboardScreen(
    onBack: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel(),
) {
    val reviews  by viewModel.reviews.collectAsState()
    val users    by viewModel.users.collectAsState()
    val listings by viewModel.listings.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    val pendingKyc      = users.count { it.kycStatus == KycStatus.PENDING_REVIEW }
    val pendingListings = listings.count { !it.isVerified }

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
                SummaryBox("$pendingListings", "Pending", WarningAmber, Modifier.weight(1f))
                SummaryBox("$pendingKyc", "KYC", ModernPrimary, Modifier.weight(1f))
                SummaryBox("${users.size}", "Users", SuccessGreen, Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            SecondaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = ModernPrimary,
                divider = { HorizontalDivider(color = ModernTextTertiary.copy(alpha = 0.1f)) }
            ) {
                TABS.forEachIndexed { i, title ->
                    Tab(
                        selected = selectedTab == i,
                        onClick = { selectedTab = i },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == i) ModernPrimary else ModernTextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == i) FontWeight.Bold else FontWeight.Medium,
                            )
                        },
                    )
                }
            }

            when (selectedTab) {
                0 -> ListingsTab(
                    listings = listings,
                    onApprove = viewModel::approveListing,
                    onRemove = viewModel::removeListing,
                )
                1 -> UsersTab(
                    users = users,
                    onBan = { uid -> viewModel.banUser(uid, true) },
                    onUnban = { uid -> viewModel.banUser(uid, false) },
                )
                2 -> KycTab(
                    pendingUsers = users.filter { it.kycStatus == KycStatus.PENDING_REVIEW },
                    onApprove = viewModel::approveKyc,
                    onReject = viewModel::rejectKyc,
                )
                3 -> ReviewsTab(
                    reviews = reviews,
                    onApprove = viewModel::approveReview,
                    onDelete = viewModel::deleteReview,
                )
                4 -> FeedbackTab()
                5 -> TicketsTab()
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

// ── Listings Tab ─────────────────────────────────────────────────────────────

@Composable
private fun ListingsTab(
    listings: List<Listing>,
    onApprove: (String) -> Unit,
    onRemove: (String) -> Unit,
) {
    var confirmRemove by remember { mutableStateOf<Listing?>(null) }

    confirmRemove?.let { listing ->
        AlertDialog(
            onDismissRequest = { confirmRemove = null },
            title = { Text("Remove Listing") },
            text = { Text("Permanently remove \"${listing.title}\"? This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = { onRemove(listing.id); confirmRemove = null },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("Remove") }
            },
            dismissButton = {
                TextButton(onClick = { confirmRemove = null }) { Text("Cancel") }
            }
        )
    }

    if (listings.isEmpty()) {
        EmptyState(Icons.Default.Home, "No listings yet")
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listings, key = { it.id }) { listing ->
            AdminActionCard(
                icon = Icons.Default.Home,
                iconColor = if (listing.isVerified) SuccessGreen else WarningAmber,
                title = listing.title.ifBlank { "Untitled" },
                subtitle = "Owner: ${listing.ownerName} · Rs ${listing.pricePerMonth}/mo",
                status = if (listing.isVerified) "LIVE" else "PENDING",
                statusColor = if (listing.isVerified) SuccessGreen else WarningAmber,
                actions = buildList {
                    if (!listing.isVerified) add("Approve" to ModernPrimary)
                    add("Remove" to ErrorRed)
                },
                onAction = { label ->
                    when (label) {
                        "Approve" -> onApprove(listing.id)
                        "Remove"  -> confirmRemove = listing
                    }
                }
            )
        }
    }
}

// ── Users Tab ────────────────────────────────────────────────────────────────

@Composable
private fun UsersTab(
    users: List<User>,
    onBan: (String) -> Unit,
    onUnban: (String) -> Unit,
) {
    var confirmBan   by remember { mutableStateOf<User?>(null) }
    var confirmUnban by remember { mutableStateOf<User?>(null) }

    confirmBan?.let { user ->
        val name = user.displayName.ifBlank { user.firstName }.ifBlank { "User" }
        AlertDialog(
            onDismissRequest = { confirmBan = null },
            title = { Text("Ban User") },
            text = { Text("Ban $name? They will not be able to log in.") },
            confirmButton = {
                Button(
                    onClick = { onBan(user.id); confirmBan = null },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("Ban") }
            },
            dismissButton = {
                TextButton(onClick = { confirmBan = null }) { Text("Cancel") }
            }
        )
    }

    confirmUnban?.let { user ->
        val name = user.displayName.ifBlank { user.firstName }.ifBlank { "User" }
        AlertDialog(
            onDismissRequest = { confirmUnban = null },
            title = { Text("Restore Account") },
            text = { Text("Restore access for $name?") },
            confirmButton = {
                Button(
                    onClick = { onUnban(user.id); confirmUnban = null },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) { Text("Restore") }
            },
            dismissButton = {
                TextButton(onClick = { confirmUnban = null }) { Text("Cancel") }
            }
        )
    }

    val nonAdminUsers = users.filter { it.role != UserRole.ADMIN }
    if (nonAdminUsers.isEmpty()) {
        EmptyState(Icons.Default.People, "No users yet")
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(nonAdminUsers, key = { it.id }) { user ->
            val name = user.displayName.ifBlank { "${user.firstName} ${user.lastName}".trim() }.ifBlank { "Unknown" }
            val active = user.isActive
            AdminActionCard(
                icon = Icons.Default.Person,
                iconColor = when {
                    !active -> ErrorRed
                    user.kycStatus == KycStatus.PENDING_REVIEW -> WarningAmber
                    else -> SuccessGreen
                },
                title = name,
                subtitle = "${user.role.name} · ${user.email}",
                status = if (active) "ACTIVE" else "BANNED",
                statusColor = if (active) SuccessGreen else ErrorRed,
                actions = if (active) listOf("Ban" to ErrorRed) else listOf("Restore" to SuccessGreen),
                onAction = { label ->
                    when (label) {
                        "Ban"     -> confirmBan = user
                        "Restore" -> confirmUnban = user
                    }
                }
            )
        }
    }
}

// ── KYC Tab ──────────────────────────────────────────────────────────────────

@Composable
private fun KycTab(
    pendingUsers: List<User>,
    onApprove: (String) -> Unit,
    onReject: (String, String) -> Unit,
) {
    var rejectingUser  by remember { mutableStateOf<User?>(null) }
    var rejectionReason by remember { mutableStateOf("") }

    rejectingUser?.let { user ->
        val name = user.displayName.ifBlank { user.firstName }.ifBlank { "Owner" }
        AlertDialog(
            onDismissRequest = { rejectingUser = null; rejectionReason = "" },
            title = { Text("Reject KYC") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enter rejection reason for $name:")
                    OutlinedTextField(
                        value = rejectionReason,
                        onValueChange = { rejectionReason = it },
                        placeholder = { Text("e.g. NIC image is unclear", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReject(user.id, rejectionReason)
                        rejectingUser = null
                        rejectionReason = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    enabled = rejectionReason.isNotBlank(),
                ) { Text("Reject") }
            },
            dismissButton = {
                TextButton(onClick = { rejectingUser = null; rejectionReason = "" }) { Text("Cancel") }
            }
        )
    }

    if (pendingUsers.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.CheckCircle, null, tint = SuccessGreen, modifier = Modifier.size(48.dp))
                Text("All KYC requests reviewed", color = ModernTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("No pending owner verifications", color = ModernTextTertiary, fontSize = 13.sp)
            }
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pendingUsers, key = { it.id }) { user ->
            val name = user.displayName.ifBlank { "${user.firstName} ${user.lastName}".trim() }.ifBlank { "Owner" }
            AdminActionCard(
                icon = Icons.Default.VerifiedUser,
                iconColor = WarningAmber,
                title = name,
                subtitle = user.email,
                status = "KYC PENDING",
                statusColor = WarningAmber,
                actions = listOf("Approve" to SuccessGreen, "Reject" to ErrorRed),
                onAction = { label ->
                    when (label) {
                        "Approve" -> onApprove(user.id)
                        "Reject"  -> rejectingUser = user
                    }
                }
            )
        }
    }
}

// ── Reviews Tab ──────────────────────────────────────────────────────────────

@Composable
private fun ReviewsTab(
    reviews: List<PlatformReview>,
    onApprove: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    if (reviews.isEmpty()) {
        EmptyState(Icons.Default.Star, "No reviews yet")
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(reviews, key = { it.id }) { review ->
            val stars = "★".repeat(review.rating) + "☆".repeat(5 - review.rating)
            AdminActionCard(
                icon = Icons.Default.Star,
                iconColor = if (review.isApproved) SuccessGreen else WarningAmber,
                title = "${review.userName}  $stars",
                subtitle = review.comment,
                status = if (review.isApproved) "APPROVED" else "PENDING",
                statusColor = if (review.isApproved) SuccessGreen else WarningAmber,
                actions = if (review.isApproved)
                    listOf("Delete" to ErrorRed)
                else
                    listOf("Approve" to SuccessGreen, "Delete" to ErrorRed),
                onAction = { label ->
                    when (label) {
                        "Approve" -> onApprove(review.id)
                        "Delete"  -> onDelete(review.id)
                    }
                },
            )
        }
    }
}

// ── Feedback Tab (stub) ───────────────────────────────────────────────────────

@Composable
private fun FeedbackTab() {
    EmptyState(Icons.Default.Feedback, "No feedback yet")
}

// ── Tickets Tab (stub) ────────────────────────────────────────────────────────

@Composable
private fun TicketsTab() {
    EmptyState(Icons.Default.SupportAgent, "No support tickets")
}

// ── Shared Composables ────────────────────────────────────────────────────────

@Composable
private fun EmptyState(icon: ImageVector, message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, null, tint = ModernTextTertiary, modifier = Modifier.size(48.dp))
            Text(message, color = ModernTextSecondary, fontSize = 16.sp)
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
    onAction: (String) -> Unit = {},
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
                                .clickable { onAction(label) }
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
