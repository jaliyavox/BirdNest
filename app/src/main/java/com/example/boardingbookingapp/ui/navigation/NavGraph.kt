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
import com.example.boardingbookingapp.ui.screens.auth.StudentRegisterScreen
import com.example.boardingbookingapp.ui.screens.auth.WelcomeScreen
import com.example.boardingbookingapp.ui.screens.review.PlatformReviewScreen
import com.example.boardingbookingapp.ui.screens.chat.ChatScreen
import com.example.boardingbookingapp.ui.screens.chat.ConversationsScreen
import com.example.boardingbookingapp.ui.screens.home.HomeScreen
import com.example.boardingbookingapp.ui.screens.home.StudentDashboardScreen
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

    fun NavHostController.navigateAfterLogin() {
        when (UserSession.currentUser.value?.role) {
            UserRole.ADMIN -> navigate(Screen.AdminDashboard.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
            else -> navigate(Screen.StudentDashboard.route) {
                popUpTo(Screen.Welcome.route) { inclusive = false }
            }
        }
    }

    NavHost(
        navController    = navController,
        startDestination = Screen.Splash.route,
        modifier         = modifier,
    ) {
        // ── Splash ────────────────────────────────────────────────────
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateNext = {
                navController.navigate(Screen.Onboarding.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        // ── Onboarding → Welcome ──────────────────────────────────────
        composable(Screen.Onboarding.route) {
            OnboardingScreen(onFinish = {
                navController.navigate(Screen.Welcome.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            })
        }

        // ── Welcome (landing) ─────────────────────────────────────────
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onRegisterStudent = { navController.navigate(Screen.StudentRegister.route) },
                onStudentLogin    = { navController.navigate(Screen.Login.route) },
                onOwnerLogin      = { navController.navigate(Screen.OwnerLogin.route) },
            )
        }

        // ── Student register ──────────────────────────────────────────
        composable(Screen.StudentRegister.route) {
            StudentRegisterScreen(
                onRegistered = {
                    navController.navigate(Screen.StudentDashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = false }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.StudentRegister.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        // ── Student login ─────────────────────────────────────────────
        composable(Screen.Login.route) {
            LoginScreen(
                onLoggedIn = { navController.navigateAfterLogin() },
                onNavigateToOwnerLogin = { navController.navigate(Screen.OwnerLogin.route) },
                onNavigateToRegister   = { navController.navigate(Screen.StudentRegister.route) },
            )
        }

        // ── OTP verify ────────────────────────────────────────────────
        composable(
            route     = Screen.OtpVerify.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType }),
        ) { back ->
            val email = back.arguments?.getString("email")?.replace("%40", "@") ?: ""
            OtpVerifyScreen(
                email     = email,
                onVerified = {
                    navController.navigate(Screen.StudentDashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        // ── Student dashboard ─────────────────────────────────────────
        composable(Screen.StudentDashboard.route) {
            StudentDashboardScreen(
                onBrowseListings = { navController.navigate(Screen.Home.route) },
                onMessages       = { navController.navigate(Screen.Conversations.route) },
                onPayments       = { navController.navigate(Screen.RentTracker.route) },
                onRoommateFinder = { navController.navigate(Screen.RoommateFinder.route) },
                onSubmitReview   = { navController.navigate(Screen.PlatformReview.route) },
                onSupportTicket  = { navController.navigate(Screen.SubmitTicket.route) },
            )
        }

        composable(Screen.PlatformReview.route) {
            PlatformReviewScreen(onBack = { navController.popBackStack() })
        }

        // ── Browse listings (old home) ────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(
                onListingClick        = { id -> navController.navigate(Screen.ListingDetail.createRoute(id)) },
                onRoommateFinderClick = { navController.navigate(Screen.RoommateFinder.route) },
                onBrowseAll           = {
                    // Navigate as a tab switch so the back stack stays clean.
                    // Tapping the Home tab from Listings will correctly return here.
                    navController.navigate(Screen.Listings.route) {
                        popUpTo(Screen.StudentDashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
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
                listingId         = id,
                isStudentSignedIn = isStudentSignedIn,
                onContactOwner    = { ownerId -> navController.navigate(Screen.Chat.createRoute(ownerId)) },
                onSignInRequired  = { navController.navigate(Screen.Welcome.route) },
                onBack            = { navController.popBackStack() },
            )
        }

        composable(Screen.PostListing.route) {
            PostListingScreen(
                onListingPosted = { navController.popBackStack() },
                onBack          = { navController.popBackStack() },
            )
        }

        // ── Roommate finder ───────────────────────────────────────────
        composable(Screen.RoommateFinder.route) {
            RoommateFinderScreen(
                onOpenProfile     = { navController.navigate(Screen.RoommateProfile.route) },
                onMessageRoommate = { uid -> navController.navigate(Screen.Chat.createRoute(uid)) },
                onBack            = { navController.popBackStack() },
            )
        }

        composable(Screen.RoommateProfile.route) {
            RoommateProfileScreen(
                onSaved = { navController.popBackStack() },
                onBack  = { navController.popBackStack() },
            )
        }

        // ── Chat ──────────────────────────────────────────────────────
        composable(Screen.Conversations.route) {
            ConversationsScreen(
                onOpenChat = { cid -> navController.navigate(Screen.Chat.createRoute(cid)) },
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

        // ── Payments ──────────────────────────────────────────────────
        composable(Screen.RentTracker.route) {
            RentTrackerScreen(
                onBack          = { navController.popBackStack() },
                onUploadReceipt = { navController.navigate(Screen.ReceiptUpload.route) },
            )
        }

        composable(Screen.ReceiptUpload.route) {
            ReceiptUploadScreen(onBack = { navController.popBackStack() })
        }

        // ── Profile ───────────────────────────────────────────────────
        composable(Screen.Profile.route) {
            ProfileScreen(
                onSignOut = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onOpenRentTracker = { navController.navigate(Screen.RentTracker.route) },
                onSubmitFeedback  = { navController.navigate(Screen.SubmitFeedback.route) },
                onSubmitTicket    = { navController.navigate(Screen.SubmitTicket.route) },
            )
        }

        composable(Screen.SubmitFeedback.route) {
            SubmitFeedbackScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.SubmitTicket.route) {
            SubmitTicketScreen(onBack = { navController.popBackStack() })
        }

        // ── Owner flow ────────────────────────────────────────────────
        composable(Screen.OwnerLogin.route) {
            OwnerLoginScreen(
                onLoggedIn            = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = false }
                    }
                },
                onNavigateToRegister  = { navController.navigate(Screen.OwnerRegister.route) },
                onBack                = { navController.popBackStack() },
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
                        popUpTo(Screen.Welcome.route) { inclusive = false }
                    }
                },
            )
        }

        // ── Admin (separate entry point) ──────────────────────────────
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(onBack = { navController.popBackStack() })
        }
    }
}
