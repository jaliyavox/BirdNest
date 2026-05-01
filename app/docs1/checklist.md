# BirdNest — Build Checklist
> Last updated: 2026-05-01

Legend: ✅ Done · 🟡 Stub/Demo only · ❌ Not started

---

## Sprint 1 — Foundation, Design System & Auth

### Dependencies
- ✅ `navigation-compose` — added
- ✅ `coil-compose` + `coil-network-okhttp` — added
- ❌ Hilt (`hilt-android`, `hilt-compiler`, `hilt-navigation-compose`) — not added
- ❌ Firebase BOM (`firebase-auth-ktx`, `firebase-firestore-ktx`, `firebase-storage-ktx`) — not added
- ❌ Poppins font in `res/font/` — not added

### Tasks
- 🟡 `libs.versions.toml` — nav + coil present; Hilt / Firebase / Room / Retrofit missing
- 🟡 `build.gradle.kts` — no Hilt plugin, no KSP plugin, no Firebase
- ❌ `MainApplication.kt` — `@HiltAndroidApp` commented out (no Hilt dep yet)
- ✅ `util/Result.kt` — sealed class present
- ✅ `util/Constants.kt` — SLIIT domain, campus coords present
- ✅ `ui/theme/Color.kt` — glassmorphism + modern palette defined
- ✅ `ui/theme/Type.kt` — typography scale present
- ✅ `ui/theme/Theme.kt` — BirdNestTheme (dark MaterialTheme)
- ✅ `ui/theme/Glass.kt` — glassBackground modifier present
- ✅ `ui/components/GradientBackground.kt` — present
- ✅ `ui/components/GlassCard.kt` — present
- ✅ `ui/components/GlassButton.kt` — present
- ✅ `ui/components/GlassTextField.kt` — present
- ✅ `ui/navigation/Screen.kt` — all routes sealed (incl. ReceiptUpload, SubmitFeedback, SubmitTicket)
- ✅ `ui/navigation/NavGraph.kt` — fully wired
- ✅ `ui/screens/splash/SplashScreen.kt` — animated, auto-navigates
- ✅ `ui/screens/auth/LoginScreen.kt` — SLIIT domain validation
- ✅ `ui/screens/auth/OtpVerifyScreen.kt` — 6-digit input, countdown, resend
- ✅ `ui/screens/auth/RegisterScreen.kt` — display name, saves to UserSession
- ✅ `ui/screens/auth/OwnerLoginScreen.kt` — email/password form
- ✅ `ui/screens/auth/OwnerRegisterScreen.kt` — name, email, password, phone
- ✅ `ui/screens/auth/KycUploadScreen.kt` — NIC + selfie slot UI
- ✅ `ui/screens/auth/KycPendingScreen.kt` — waiting state
- ✅ `data/model/User.kt` — kycStatus, isEmailVerified, UserRole, KycStatus
- 🟡 `di/AppModule.kt` — stub; no real Firebase injection
- 🟡 `ui/screens/auth/AuthViewModel.kt` — demo mode (any 6-digit OTP passes; no real Resend call)
- ✅ `MainActivity.kt` — NavGraph host + animated BottomNavBar

**Remaining for full Sprint 1 deliverable:**
- Add Firebase `google-services.json` to app/
- Add Firebase deps + Hilt to build files
- Wire real Firebase Auth + Resend OTP in AuthViewModel
- Enable `@HiltAndroidApp` in MainApplication

---

## Sprint 2 — Listings Feed & Smart Search

### Dependencies
- ❌ Room `2.6.1` + KSP — not added
- ❌ Retrofit `2.11.0` + `converter-gson` + OkHttp logging — not added
- ❌ Google Maps Compose `4.4.1` — not added

### Tasks
- ✅ `data/model/Listing.kt` — full model with all fields
- 🟡 `data/local/entity/ListingEntity.kt` — class exists; `@Entity` annotation not added (needs Room dep)
- 🟡 `data/local/dao/ListingDao.kt` — class exists; `@Dao`/`@Query` not added (needs Room dep)
- 🟡 `data/local/AppDatabase.kt` — class exists; `@Database` not added (needs Room dep)
- ✅ `data/remote/dto/ListingDto.kt` — Firestore mapping stubbed
- 🟡 `data/repository/ListingRepository.kt` — stub; no real Firestore/Room reads
- 🟡 `domain/usecase/listing/GetListingsUseCase.kt` — stub
- 🟡 `domain/usecase/listing/FilterListingsUseCase.kt` — stub
- 🟡 `di/DatabaseModule.kt` — stub; Room not wired
- 🟡 `di/NetworkModule.kt` — stub; Retrofit not wired
- ✅ `ui/components/ListingCard.kt` — price, distance, verified badge, type chips
- ✅ `ui/components/BottomNavBar.kt` — wired to NavController
- ✅ `ui/screens/home/HomeViewModel.kt` — mock listings + filter/search logic
- ✅ `ui/screens/home/HomeScreen.kt` — search bar, filter chips, LazyColumn, roommate promo
- ✅ `ui/screens/listings/ListingsScreen.kt` — search, filter panel, price slider
- 🟡 `ui/screens/listings/ListingDetailScreen.kt` — UI complete; Google Maps placeholder missing

**Remaining for full Sprint 2 deliverable:**
- Add Google Maps API key to `local.properties`
- Add Maps Compose dep and embed MapView in ListingDetailScreen
- Add Room + Retrofit deps and annotate entity/DAO/database

---

## Sprint 3 — Post Listings & Reviews

### Tasks
- 🟡 `domain/usecase/listing/PostListingUseCase.kt` — stub
- ✅ `data/model/Review.kt` — model present
- 🟡 `ui/screens/listings/ListingsViewModel.kt` — stub; no real post/edit/delete
- ✅ `ui/screens/listings/PostListingScreen.kt` — 5-step form (Photos→Details→Amenities→Location→Review)
- ❌ Firebase Storage photo upload with progress bar — not implemented
- ❌ Review section in `ListingDetailScreen` — not implemented (star rating + reviews list missing)
- ❌ "Pending Admin Review" banner for unverified listings — not implemented

**Remaining for full Sprint 3 deliverable:**
- Add star-rating input + reviews LazyColumn to ListingDetailScreen
- Show "Pending Admin Review" banner in PostListingScreen / owner view
- Wire Firebase Storage upload with progress indicator in PostListingScreen

---

## Sprint 4 — Roommate Finder

### Tasks
- ✅ `data/model/RoommateProfile.kt` — full model with preferences
- 🟡 `data/repository/RoommateRepository.kt` — stub
- ✅ `domain/usecase/roommate/GetRoommateMatchesUseCase.kt` — weighted scoring implemented
- ✅ `ui/components/RoommateCard.kt` — avatar, match %, hobby tags
- 🟡 `ui/screens/roommate/RoommateViewModel.kt` — stub
- ✅ `ui/screens/roommate/RoommateProfileScreen.kt` — create/edit profile + privacy toggle
- ✅ `ui/screens/roommate/RoommateFinderScreen.kt` — card stack + list toggle

**Remaining for full Sprint 4 deliverable:**
- Wire RoommateRepository to Firestore
- Real compatibility scoring against live profiles

---

## Sprint 5 — Real-Time Chat

### Tasks
- ✅ `data/model/ChatMessage.kt` — model present
- 🟡 `data/repository/ChatRepository.kt` — stub; no Firestore snapshots
- 🟡 `domain/usecase/chat/GetMessagesUseCase.kt` — stub
- 🟡 `domain/usecase/chat/SendMessageUseCase.kt` — stub
- 🟡 `ui/screens/chat/ChatViewModel.kt` — stub
- ✅ `ui/screens/chat/ConversationsScreen.kt` — list, unread badge, last message (mock)
- ✅ `ui/screens/chat/ChatScreen.kt` — message bubbles, send field (mock)
- ✅ `data/local/entity/ChatMessageEntity.kt` + `ChatDao.kt` — local cache models present
- ❌ Firebase Cloud Messaging — not implemented

**Remaining for full Sprint 5 deliverable:**
- Wire ChatRepository to Firestore real-time snapshots
- Implement FCM push on new message
- Connect ChatViewModel to real repository

---

## Sprint 6 — Payments & Receipt Upload

### Tasks
- ✅ `data/model/Payment.kt` — receiptUrl, receiptVerified, SplitBill model
- 🟡 `data/repository/PaymentRepository.kt` — stub; no Firebase Storage
- 🟡 `ui/screens/payment/PaymentViewModel.kt` — stub
- ✅ `ui/screens/payment/RentTrackerScreen.kt` — ledger, history, status badges, Upload Receipt button wired
- ✅ `ui/screens/payment/ReceiptUploadScreen.kt` — photo attach UI, notes, submit → success state *(created today)*
- ❌ Owner receipt review: notification + confirm/reject in admin/owner dashboard — not implemented
- ❌ Split-bill screen (add housemates by email, divide total, individual receipts) — not implemented
- ❌ Shuttle proximity chip on `ListingDetailScreen` — not implemented

**Remaining for full Sprint 6 deliverable:**
- Wire ReceiptUploadScreen to real Firebase Storage upload
- Add owner confirm/reject UI in AdminDashboard or dedicated owner screen
- Build SplitBillScreen

---

## Sprint 7 — Admin Panel & Final Polish

### Tasks
- ✅ `AdminDashboardScreen.kt` — 4 tabs: Listings / Users / Feedback / Support
- ✅ `data/model/SupportTicket.kt` — full model
- ✅ `data/model/Feedback.kt` — full model
- 🟡 `ui/screens/admin/AdminViewModel.kt` — stub
- ✅ `ui/screens/profile/ProfileScreen.kt` — avatar, badges, stats, settings, Feedback + Ticket buttons
- ✅ `ui/screens/profile/SubmitFeedbackScreen.kt` — category chips, message, submit → success *(created today)*
- ✅ `ui/screens/profile/SubmitTicketScreen.kt` — subject, priority, description, submit → ticket ref *(created today)*
- ❌ Dispute/report flow (report button on listings + roommate profiles → Firestore `reports`) — not implemented
- ❌ Haptic feedback on key actions — not implemented
- ❌ Shared element transitions (listing card → detail) — not implemented
- ❌ Dynamic status bar tint — not implemented
- ❌ Final QA pass — pending

---

## External Setup Required (blocks Firebase features)

| Item | Status |
|---|---|
| Firebase project created + `google-services.json` added | ❌ |
| Resend API key provided (for OTP emails) | ❌ |
| Google Maps API key added to `local.properties` | ❌ |
| Hilt + Firebase + Room + Retrofit added to build files | ❌ |

---

## Summary

| Sprint | UI / Demo | Real Backend |
|---|---|---|
| 1 — Auth | ✅ Complete | ❌ Firebase not wired |
| 2 — Listings | ✅ Complete (no Maps) | ❌ Room / Firestore not wired |
| 3 — Post & Reviews | 🟡 Post form done; reviews missing | ❌ |
| 4 — Roommate | ✅ Complete | ❌ Firestore not wired |
| 5 — Chat | ✅ Complete | ❌ Firestore / FCM not wired |
| 6 — Payments | ✅ Complete | ❌ Storage not wired |
| 7 — Admin & Polish | 🟡 Screens done; animations missing | ❌ |
