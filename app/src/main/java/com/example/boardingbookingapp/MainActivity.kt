package com.example.birdnest

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
import com.example.birdnest.ui.components.BottomNavBar
import com.example.birdnest.ui.navigation.NavGraph
import com.example.birdnest.ui.navigation.Screen
import com.example.birdnest.ui.theme.BirdNestTheme
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
                                    popUpTo(Screen.Home.route) { saveState = true }
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
