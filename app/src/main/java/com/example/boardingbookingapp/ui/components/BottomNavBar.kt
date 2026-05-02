package com.example.boardingbookingapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.boardingbookingapp.data.auth.UserSession
import com.example.boardingbookingapp.data.model.UserRole
import com.example.boardingbookingapp.ui.navigation.Screen
import com.example.boardingbookingapp.ui.theme.*

@Composable
fun BottomNavBar(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
) {
    val currentUser by UserSession.currentUser.collectAsState()
    val isOwner = currentUser?.role == UserRole.OWNER

    val studentTabs = listOf(
        Triple("Home",    Icons.Default.Home,                  Screen.StudentDashboard.route),
        Triple("Search",  Icons.Default.Search,                Screen.Listings.route),
        Triple("Chat",    Icons.AutoMirrored.Filled.Chat,      Screen.Conversations.route),
        Triple("Profile", Icons.Default.Person,                Screen.Profile.route),
    )
    val ownerTabs = listOf(
        Triple("Home",    Icons.Default.Home,                  Screen.Home.route),
        Triple("Search",  Icons.Default.Search,                Screen.Listings.route),
        Triple("Post",    Icons.Default.Add,                   Screen.PostListing.route),
        Triple("Chat",    Icons.AutoMirrored.Filled.Chat,      Screen.Conversations.route),
        Triple("Profile", Icons.Default.Person,                Screen.Profile.route),
    )
    val tabs = if (isOwner) ownerTabs else studentTabs

    NavigationBar(
        containerColor = ModernCard,
        tonalElevation = 8.dp
    ) {
        tabs.forEach { (label, icon, route) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(route) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ModernPrimary,
                    selectedTextColor = ModernPrimary,
                    unselectedIconColor = ModernTextTertiary,
                    unselectedTextColor = ModernTextTertiary,
                    indicatorColor = ModernBlueSoft,
                )
            )
        }
    }
}
