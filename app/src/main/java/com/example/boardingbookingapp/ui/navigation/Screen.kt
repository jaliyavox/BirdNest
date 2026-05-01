package com.example.boardingbookingapp.ui.navigation

sealed class Screen(val route: String) {
    data object Splash          : Screen("splash")
    data object Onboarding      : Screen("onboarding")
    data object Login           : Screen("login")
    data object OtpVerify       : Screen("otp_verify/{email}") {
        fun createRoute(email: String) = "otp_verify/${email.replace("@", "%40")}"
    }
    data object Register        : Screen("register")
    data object OwnerLogin      : Screen("owner_login")
    data object OwnerRegister   : Screen("owner_register")
    data object KycUpload       : Screen("kyc_upload")
    data object KycPending      : Screen("kyc_pending")
    data object Home            : Screen("home")
    data object Listings        : Screen("listings")
    data object ListingDetail   : Screen("listing/{listingId}") {
        fun createRoute(id: String) = "listing/$id"
    }
    data object PostListing     : Screen("post_listing")
    data object RoommateFinder  : Screen("roommate_finder")
    data object RoommateProfile : Screen("roommate_profile")
    data object Conversations   : Screen("conversations")
    data object Chat            : Screen("chat/{conversationId}") {
        fun createRoute(id: String) = "chat/$id"
    }
    data object RentTracker     : Screen("rent_tracker")
    data object Profile         : Screen("profile")
    data object AdminDashboard  : Screen("admin")
    data object ReceiptUpload  : Screen("receipt_upload")
    data object SubmitFeedback : Screen("submit_feedback")
    data object SubmitTicket   : Screen("submit_ticket")
}
