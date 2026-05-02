package com.example.boardingbookingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.boardingbookingapp.ui.components.BottomNavBar
import com.example.boardingbookingapp.ui.navigation.NavGraph
import com.example.boardingbookingapp.ui.navigation.Screen
import com.example.boardingbookingapp.ui.theme.BirdNestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BirdNestTheme {
                val navController = rememberNavController()
                val navBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStack?.destination?.route

                // Bottom bar is shown on all main tab screens but NOT on StudentDashboard
                // (StudentDashboard is the post-login entry hub, not a tab itself)
                val bottomBarRoutes = setOf(
                    Screen.Home.route,
                    Screen.Listings.route,
                    Screen.Conversations.route,
                    Screen.Profile.route,
                )
                val showBottomBar = currentRoute in bottomBarRoutes

                Box(modifier = Modifier.fillMaxSize()) {
                    NavGraph(navController = navController)

                    AnimatedVisibility(
                        visible = showBottomBar,
                        modifier = Modifier.align(Alignment.BottomCenter),
                        enter = fadeIn() + slideInVertically { it },
                        exit  = fadeOut() + slideOutVertically { it },
                    ) {
                        BottomNavBar(
                            currentRoute = currentRoute ?: "",
                            onTabSelected = { route ->
                                navController.navigate(route) {
                                    // Use StudentDashboard as anchor — the true root after login.
                                    // This ensures ALL tab-level back stack entries are cleared
                                    // and state is saved/restored correctly when switching tabs.
                                    popUpTo(Screen.StudentDashboard.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
