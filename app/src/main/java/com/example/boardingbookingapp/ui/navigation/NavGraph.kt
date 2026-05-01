package com.example.boardingbookingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.boardingbookingapp.ui.screens.admin.AdminDashboardScreen
import com.example.boardingbookingapp.ui.screens.auth.KycPendingScreen
import com.example.boardingbookingapp.ui.screens.auth.KycUploadScreen
import com.example.boardingbookingapp.ui.screens.auth.LoginScreen
import com.example.boardingbookingapp.ui.screens.auth.OtpVerifyScreen
import com.example.boardingbookingapp.ui.screens.auth.OwnerLoginScreen
import com.example.boardingbookingapp.ui.screens.auth.OwnerRegisterScreen
import com.example.boardingbookingapp.ui.screens.auth.RegisterScreen
import com.example.boardingbookingapp.ui.screens.chat.ChatScreen
import com.example.boardingbookingapp.ui.screens.chat.ConversationsScreen
import com.example.boardingbookingapp.ui.screens.home.HomeScreen
import com.example.boardingbookingapp.ui.screens.listings.ListingDetailScreen
import com.example.boardingbookingapp.ui.screens.listings.ListingsScreen
import com.example.boardingbookingapp.ui.screens.listings.PostListingScreen
import com.example.boardingbookingapp.ui.screens.onboarding.OnboardingScreen
import com.example.boardingbookingapp.ui.screens.payment.ReceiptUploadScreen
import com.example.boardingbookingapp.ui.screens.payment.RentTrackerScreen
import com.example.boardingbookingapp.ui.screens.profile.ProfileScreen
import com.example.boardingbookingapp.ui.screens.profile.SubmitFeedbackScreen
import com.example.boardingbookingapp.ui.screens.profile.SubmitTicketScreen
import com.example.boardingbookingapp.ui.screens.roommate.RoommateFinderScreen
import com.example.boardingbookingapp.ui.screens.roommate.RoommateProfileScreen
import com.example.boardingbookingapp.ui.screens.splash.SplashScreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.boardingbookingapp.data.auth.UserSession
import com.example.boardingbookingapp.data.model.UserRole

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val currentUser by UserSession.currentUser.collectAsState()
    val isStudentSignedIn = currentUser?.role == UserRole.STUDENT

    NavHost(
        navController    = navController,
        startDestination = Screen.Splash.route,
        modifier         = modifier,
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateNext = {
                navController.navigate(Screen.Onboarding.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(onFinish = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onOtpSent = { email ->
                    navController.navigate(Screen.OtpVerify.createRoute(email))
                },
                onNavigateToOwnerLogin = {
                    navController.navigate(Screen.OwnerLogin.route)
                },
            )
        }

        composable(
            route     = Screen.OtpVerify.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType }),
        ) { back ->
            val email = back.arguments?.getString("email")?.replace("%40", "@") ?: ""
            OtpVerifyScreen(
                email      = email,
                onVerified = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistrationComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
            )
        }

        composable(Screen.OwnerLogin.route) {
            OwnerLoginScreen(
                onLoggedIn = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.OwnerRegister.route)
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.OwnerRegister.route) {
            OwnerRegisterScreen(
                onRegistered = {
                    navController.navigate(Screen.KycUpload.route) {
                        popUpTo(Screen.OwnerRegister.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.KycUpload.route) {
            KycUploadScreen(
                onSubmitted = {
                    navController.navigate(Screen.KycPending.route) {
                        popUpTo(Screen.KycUpload.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.KycPending.route) {
            KycPendingScreen(
                onApproved = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onListingClick = { id ->
                    navController.navigate(Screen.ListingDetail.createRoute(id))
                },
                onRoommateFinderClick = {
                    navController.navigate(Screen.RoommateFinder.route)
                },
            )
        }

        composable(Screen.Listings.route) {
            ListingsScreen(
                onListingClick = { id ->
                    navController.navigate(Screen.ListingDetail.createRoute(id))
                },
            )
        }

        composable(
            route     = Screen.ListingDetail.route,
            arguments = listOf(navArgument("listingId") { type = NavType.StringType }),
        ) { back ->
            val id = back.arguments?.getString("listingId") ?: ""
            ListingDetailScreen(
                listingId     = id,
                isStudentSignedIn = isStudentSignedIn,
                onContactOwner = { ownerId ->
                    navController.navigate(Screen.Chat.createRoute(ownerId))
                },
                onSignInRequired = {
                    navController.navigate(Screen.Login.route)
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.PostListing.route) {
            PostListingScreen(
                onListingPosted = { navController.popBackStack() },
                onBack          = { navController.popBackStack() },
            )
        }

        composable(Screen.RoommateFinder.route) {
            RoommateFinderScreen(
                onOpenProfile      = { navController.navigate(Screen.RoommateProfile.route) },
                onMessageRoommate  = { uid -> navController.navigate(Screen.Chat.createRoute(uid)) },
                onBack             = { navController.popBackStack() },
            )
        }

        composable(Screen.RoommateProfile.route) {
            RoommateProfileScreen(
                onSaved = { navController.popBackStack() },
                onBack  = { navController.popBackStack() },
            )
        }

        composable(Screen.Conversations.route) {
            ConversationsScreen(
                onOpenChat = { cid ->
                    navController.navigate(Screen.Chat.createRoute(cid))
                },
            )
        }

        composable(
            route     = Screen.Chat.route,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType }),
        ) { back ->
            val cid = back.arguments?.getString("conversationId") ?: ""
            ChatScreen(
                conversationId = cid,
                onBack         = { navController.popBackStack() },
            )
        }

        composable(Screen.RentTracker.route) {
            RentTrackerScreen(
                onBack = { navController.popBackStack() },
                onUploadReceipt = { navController.navigate(Screen.ReceiptUpload.route) },
            )
        }

        composable(Screen.ReceiptUpload.route) {
            ReceiptUploadScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onOpenRentTracker = { navController.navigate(Screen.RentTracker.route) },
                onOpenAdmin = { navController.navigate(Screen.AdminDashboard.route) },
                onSubmitFeedback = { navController.navigate(Screen.SubmitFeedback.route) },
                onSubmitTicket = { navController.navigate(Screen.SubmitTicket.route) },
            )
        }

        composable(Screen.SubmitFeedback.route) {
            SubmitFeedbackScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.SubmitTicket.route) {
            SubmitTicketScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
