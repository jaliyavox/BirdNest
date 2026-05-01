# BirdNest — Sprint Plan
> Housing & Boarding Finder for SLIIT University Students

---

## Decisions (locked in)

| Decision | Choice |
|---|---|
| App name | **BirdNest** |
| Package | `com.example.birdnest` |
| Backend | **Firebase only** (Auth + Firestore + Storage) |
| Maps | **Google Maps Compose** (bring your own API key) |
| Roommate matching | **Simple weighted scoring** |
| Payment gateway | **None** — tenants upload payment receipt image; owner confirms |
| Push notifications | **Firebase Cloud Messaging** |
| Student auth | Email/password + **Resend API** OTP to @my.sliit.lk (API key provided later) |
| Owner auth | Email/password + **KYC** (NIC + selfie upload → admin approves) |
| Owner verification | Manual admin approval after KYC document review |

---

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Design System | Glassmorphism (frosted glass, deep blue/violet/cyan) |
| Navigation | Navigation Compose |
| DI | Hilt |
| Local DB | Room + KSP |
| Networking | Retrofit 2 + OkHttp |
| Auth | Firebase Auth + Resend email OTP |
| Realtime / Storage | Firebase Firestore + Firebase Storage |
| Maps | Google Maps Compose |
| Image Loading | Coil 3 |
| State | ViewModel + StateFlow + Coroutines |
| Fonts | Poppins (Google Fonts) |

---

## Full Project Structure

```
com.example.birdnest/
│
├── MainApplication.kt               ← @HiltAndroidApp
├── MainActivity.kt                  ← single activity, NavGraph host
│
├── data/
│   ├── model/
│   │   ├── User.kt                  ← id, email, role, kycStatus, isEmailVerified
│   │   ├── Listing.kt
│   │   ├── RoommateProfile.kt
│   │   ├── ChatMessage.kt
│   │   ├── Payment.kt               ← receiptUrl, receiptVerified (no PayHere)
│   │   ├── Review.kt
│   │   ├── SupportTicket.kt         ← id, subject, status, priority
│   │   └── Feedback.kt              ← id, category, message, adminReply
│   ├── local/  (Room cache)
│   ├── remote/ (Firestore DTOs + Retrofit stubs)
│   └── repository/
│       ├── AuthRepository.kt        ← sendOtp, verifyOtp, submitKyc
│       ├── ListingRepository.kt
│       ├── ChatRepository.kt
│       ├── RoommateRepository.kt
│       └── PaymentRepository.kt     ← uploadReceipt, verifyReceipt
│
├── domain/usecase/ (auth, listing, roommate, chat)
│
├── ui/
│   ├── theme/        (Color, Theme, Type, Glass)
│   ├── components/   (GlassCard, GlassButton, GlassTextField, etc.)
│   ├── screens/
│   │   ├── splash/
│   │   ├── auth/       (Login, Register/OTP, OwnerRegister, KycUpload)
│   │   ├── home/
│   │   ├── listings/   (Feed, Detail, Post)
│   │   ├── roommate/   (Finder, Profile)
│   │   ├── chat/       (Conversations, Chat)
│   │   ├── payment/    (RentTracker, ReceiptUpload)
│   │   ├── profile/
│   │   └── admin/      (Dashboard: Listings / Users / Feedback / Tickets)
│   └── navigation/   (Screen, NavGraph)
│
├── di/    (AppModule, NetworkModule, DatabaseModule)
└── util/  (Constants, Extensions, Result)
```

---

## Sprint 1 — Foundation, Design System & Auth
**Goal:** Working auth for students (OTP) + owners (KYC), glassmorphism UI shell  
**Duration:** ~2 weeks

### Dependencies to add
- `androidx.navigation:navigation-compose:2.7.7`
- `com.google.dagger:hilt-android:2.51.1` + hilt-compiler + `hilt-navigation-compose`
- Firebase BOM `33.x` → `firebase-auth-ktx`, `firebase-firestore-ktx`, `firebase-storage-ktx`
- `io.coil-kt.coil3:coil-compose`
- Poppins font in `res/font/`

### Tasks
- [ ] `libs.versions.toml` — add Sprint 1 versions + library aliases
- [ ] `build.gradle.kts` — apply Hilt plugin, KSP plugin, add Sprint 1 deps
- [ ] `MainApplication.kt` — `@HiltAndroidApp`
- [ ] `util/Result.kt` — sealed Result<T> (stubbed)
- [ ] `util/Constants.kt` — SLIIT_EMAIL_DOMAIN, campus coords, Firestore collection names (stubbed)
- [ ] `ui/theme/Color.kt` — glassmorphism palette (Deep Blue #0A0E27, Violet #6C3FC5, Cyan #00D4FF)
- [ ] `ui/theme/Type.kt` — Poppins typography scale
- [ ] `ui/theme/Theme.kt` — dark MaterialTheme
- [ ] `ui/theme/Glass.kt` — glassBackground() Modifier with blur/tint
- [ ] `ui/components/GradientBackground.kt` — animated radial gradient (stubbed)
- [ ] `ui/components/GlassCard.kt` — frosted container with 1px border (stubbed)
- [ ] `ui/components/GlassButton.kt` — gradient CTA (stubbed)
- [ ] `ui/components/GlassTextField.kt` — semi-transparent input (stubbed)
- [ ] `ui/navigation/Screen.kt` — sealed class all routes (stubbed)
- [ ] `ui/navigation/NavGraph.kt` — NavHost wired to all routes
- [ ] `ui/screens/splash/SplashScreen.kt` — BirdNest logo + animated gradient, 2s auto-navigate
- [ ] **Student auth flow:**
  - [ ] `ui/screens/auth/LoginScreen.kt` — email input, "Send OTP" button (SLIIT domain only)
  - [ ] `ui/screens/auth/OtpVerifyScreen.kt` — 6-digit OTP input + verify button
  - [ ] `ui/screens/auth/RegisterScreen.kt` — display name + role = STUDENT, save profile
- [ ] **Owner auth flow:**
  - [ ] `ui/screens/auth/OwnerLoginScreen.kt` — email/password login
  - [ ] `ui/screens/auth/OwnerRegisterScreen.kt` — email, password, phone
  - [ ] `ui/screens/auth/KycUploadScreen.kt` — NIC photo picker + selfie picker + submit
- [ ] `data/model/User.kt` — includes kycStatus, isEmailVerified (done)
- [ ] `di/AppModule.kt` — provide FirebaseAuth, FirebaseFirestore, FirebaseStorage
- [ ] `ui/screens/auth/AuthViewModel.kt` — sendOtp(), verifyOtp(), registerOwner(), submitKyc()
- [ ] `MainActivity.kt` — wire NavGraph, auth state redirect (unauthenticated → Login; student unverified → OTP; owner KYC_PENDING → KycUpload; verified → Home)

**Sprint 1 Deliverable:** Students receive OTP via Resend to @my.sliit.lk and log in. Owners register with KYC upload. App navigates correctly based on auth state.

---

## Sprint 2 — Listings Feed & Smart Search
**Goal:** Browse listings, smart filters, listing detail with Google Maps  
**Duration:** ~2 weeks

### Dependencies to add
- Room `2.6.1` + KSP annotation processor
- Retrofit `2.11.0` + `converter-gson` + OkHttp logging
- Google Maps Compose `4.4.1` + Maps SDK

### Tasks
- [ ] `data/model/Listing.kt` — full model (stubbed)
- [ ] `data/local/entity/ListingEntity.kt` + `@Entity` annotation (needs @Entity)
- [ ] `data/local/dao/ListingDao.kt` + `@Dao` with `@Query` methods
- [ ] `data/local/AppDatabase.kt` — `@Database` with ListingEntity
- [ ] `data/remote/dto/ListingDto.kt` — Firestore document mapping (stubbed)
- [ ] `data/repository/ListingRepository.kt` — offline-first Room + Firestore
- [ ] `domain/usecase/listing/GetListingsUseCase.kt` (stubbed)
- [ ] `domain/usecase/listing/FilterListingsUseCase.kt` (stubbed)
- [ ] `di/DatabaseModule.kt` — Room + DAOs
- [ ] `di/NetworkModule.kt` — Retrofit + OkHttp
- [ ] `ui/components/ListingCard.kt` — Coil photo, price, distance, verified badge (stubbed)
- [ ] `ui/components/BottomNavBar.kt` — wired to NavController (stubbed)
- [ ] `ui/screens/home/HomeViewModel.kt` — listings StateFlow, filter state (stubbed)
- [ ] `ui/screens/home/HomeScreen.kt` — search bar, filter chips, LazyColumn
- [ ] `ui/screens/listings/ListingDetailScreen.kt` — photo pager, amenity chips, Google Map, contact button

**Sprint 2 Deliverable:** Feed, search, filter, listing detail with map all working.

---

## Sprint 3 — Post Listings & Reviews
**Goal:** Owners post listings with photos; students rate + review  
**Duration:** ~2 weeks

### Tasks
- [ ] `domain/usecase/listing/PostListingUseCase.kt` (stubbed)
- [ ] `data/model/Review.kt` (done)
- [ ] `ui/screens/listings/ListingsViewModel.kt` — post, edit, delete, review state
- [ ] `ui/screens/listings/PostListingScreen.kt` — multi-step: Photos → Details → Amenities → Map pin → Submit
- [ ] Firebase Storage photo upload with progress bar
- [ ] Review section in `ListingDetailScreen` — star rating input + reviews list
- [ ] Unverified listings show "Pending Admin Review" banner to owner; hidden from student feed

**Sprint 3 Deliverable:** Owners post listings; students review; unverified listings gated.

---

## Sprint 4 — Roommate Finder
**Goal:** Students browse roommate matches ranked by compatibility  
**Duration:** ~2 weeks

### Tasks
- [ ] `data/model/RoommateProfile.kt` (done)
- [ ] `data/repository/RoommateRepository.kt` (stubbed)
- [ ] `domain/usecase/roommate/GetRoommateMatchesUseCase.kt` — weighted score (study, sleep, smoking, hobbies, budget) (implemented)
- [ ] `ui/components/RoommateCard.kt` — avatar, name, match %, hobby tags (stubbed)
- [ ] `ui/screens/roommate/RoommateViewModel.kt` (stubbed)
- [ ] `ui/screens/roommate/RoommateProfileScreen.kt` — create/edit profile + privacy toggle
- [ ] `ui/screens/roommate/RoommateFinderScreen.kt` — swipeable card stack + list toggle

**Sprint 4 Deliverable:** Students create profiles, browse ranked roommate matches, see compatibility breakdown.

---

## Sprint 5 — Real-Time Chat
**Goal:** In-app messaging with image sharing  
**Duration:** ~2 weeks

### Tasks
- [ ] `data/model/ChatMessage.kt` (done)
- [ ] `data/repository/ChatRepository.kt` — Firestore snapshots
- [ ] `domain/usecase/chat/GetMessagesUseCase.kt` (stubbed)
- [ ] `domain/usecase/chat/SendMessageUseCase.kt` (stubbed)
- [ ] `ui/screens/chat/ChatViewModel.kt` (stubbed)
- [ ] `ui/screens/chat/ConversationsScreen.kt` — list with unread badge, last message
- [ ] `ui/screens/chat/ChatScreen.kt` — message bubbles, image picker, send
- [ ] `data/local/entity/ChatMessageEntity.kt` + `ChatDao.kt` — local cache
- [ ] Firebase Cloud Messaging — push on new message

**Sprint 5 Deliverable:** Real-time chat between students and owners, with image sharing and push notifications.

---

## Sprint 6 — Payments & Receipt Upload
**Goal:** Students upload rent receipts; owners confirm; split-bill utility  
**Duration:** ~2 weeks

> **No payment gateway.** Students pay rent offline and upload a photo of the payment receipt. The owner reviews and marks it verified.

### Tasks
- [ ] `data/model/Payment.kt` — includes receiptUrl, receiptVerified (updated)
- [ ] `data/repository/PaymentRepository.kt` — uploadReceipt(), verifyReceipt() (updated)
- [ ] `ui/screens/payment/PaymentViewModel.kt` (stubbed)
- [ ] `ui/screens/payment/RentTrackerScreen.kt` — monthly ledger, payment history, receipt status
- [ ] New: `ui/screens/payment/ReceiptUploadScreen.kt` — image picker, upload to Firebase Storage, submit for owner review
- [ ] Owner receipt review: notification + confirm/reject flow in owner dashboard
- [ ] Split-bill: add housemates by email, divide total, each person uploads own receipt
- [ ] Shuttle proximity chip on `ListingDetailScreen`

**Sprint 6 Deliverable:** Tenant uploads receipt → owner confirms → status updates to PAID.

---

## Sprint 7 — Admin Panel & Final Polish
**Goal:** Full admin control, user management, feedback, support tickets, polish  
**Duration:** ~2 weeks

### Tasks
- [ ] **Admin Dashboard (`AdminDashboardScreen.kt`)** — 4 tabs:
  - **Listings** — approve/reject pending listings, view all
  - **Users** — view all users, activate/deactivate accounts, approve owner KYC
  - **Feedback** — view submitted feedback by category, reply, mark resolved
  - **Support Tickets** — view open tickets, change status, add admin notes
- [ ] `data/model/SupportTicket.kt` (done)
- [ ] `data/model/Feedback.kt` (done)
- [ ] `ui/screens/admin/AdminViewModel.kt` — all admin state (stubbed)
- [ ] `ui/screens/profile/ProfileScreen.kt` — avatar, role badge, my listings, my reviews, submit feedback/ticket buttons (stubbed)
- [ ] New: `ui/screens/profile/SubmitFeedbackScreen.kt`
- [ ] New: `ui/screens/profile/SubmitTicketScreen.kt`
- [ ] Dispute/report flow — report button on listings + roommate profiles → Firestore `reports`
- [ ] Haptic feedback on key actions
- [ ] Shared element transitions (listing card → detail)
- [ ] Edge-to-edge polish, dynamic status bar tint
- [ ] Final QA pass

**Sprint 7 Deliverable:** Production-ready app with full admin panel, user management, feedback + support ticket system, and polished animations.

---

## Notes for build time
- Resend API key → will be provided before Sprint 1 starts (used in a Firebase Cloud Function to send OTP emails)
- Google Maps API key → needed for Sprint 2 (add to `local.properties` as `MAPS_API_KEY`)
- Firebase `google-services.json` → needed before Sprint 1; download from Firebase Console after creating the project
