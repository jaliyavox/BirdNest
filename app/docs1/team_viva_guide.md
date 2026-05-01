
# BirdNest — Team Viva Guide
> 6 members · Each section covers: owned files, feature overview, data flow, and likely viva questions.

---

## Member 1 — Admin Panel & User Management + Student Dashboard
**Part owned by:** [Your name — jaliyathegreat@gmail.com]

### Files responsible for
```
ui/screens/admin/
  AdminDashboardScreen.kt      ← 4-tab admin UI
  AdminViewModel.kt            ← all admin state + operations

ui/screens/home/
  HomeScreen.kt                ← student landing page after login
  HomeViewModel.kt

ui/screens/profile/
  ProfileScreen.kt
  ProfileViewModel.kt
  SubmitFeedbackScreen.kt
  SubmitTicketScreen.kt

data/model/
  User.kt                      ← UserRole, KycStatus enums
  SupportTicket.kt
  Feedback.kt

data/repository/
  AuthRepository.kt            ← sendOtp, verifyOtp, submitKyc, user management

ui/screens/auth/
  AuthViewModel.kt             ← complete auth state machine
  LoginScreen.kt
  OtpVerifyScreen.kt
  RegisterScreen.kt
  OwnerLoginScreen.kt
  OwnerRegisterScreen.kt
  KycUploadScreen.kt
```

### Feature overview
- **Student auth:** Student enters @my.sliit.lk email → Resend API sends OTP via Firebase Function → student enters 6-digit OTP → Firebase Auth creates/signs in user
- **Owner auth:** Email/password signup → uploads NIC photo + selfie (Firebase Storage) → KycStatus = PENDING_REVIEW → admin approves → KycStatus = APPROVED
- **Student dashboard (HomeScreen):** Listing feed with search + filter chips; quick links to Roommate Finder and Chat
- **Admin panel tabs:**
  - **Listings:** Approve / reject pending listings (set `isVerified = true/false` in Firestore)
  - **Users:** View all users; activate/deactivate (`isActive`); approve owner KYC (set `kycStatus = APPROVED`)
  - **Feedback:** View `feedbacks` Firestore collection; reply; mark resolved
  - **Support Tickets:** View `support_tickets` collection; update `TicketStatus`; add admin notes

### Data flow
```
Student Login:
  LoginScreen → AuthViewModel.sendOtp(email) →
  Firebase Function → Resend API → email sent →
  OtpVerifyScreen → AuthViewModel.verifyOtp(email, otp) →
  Firebase Auth signInWithCustomToken → User saved to Firestore → HomeScreen

Owner KYC:
  KycUploadScreen → AuthViewModel.submitKyc(nicUri, selfieUri) →
  Firebase Storage upload → Firestore user.kycStatus = PENDING_REVIEW →
  Admin reviews in AdminDashboard → sets kycStatus = APPROVED
```

### Likely viva questions
- How does the Resend OTP flow work end-to-end? Why use Firebase Functions as middleware?
- What prevents a non-SLIIT email from registering as a student?
- How is the admin role protected from being self-assigned?
- What is the KYC flow for owners and why is it needed?
- How does the admin activate/deactivate a user account?
- What Firestore collections does the admin screen read from?
- How are support tickets different from feedback?

---

## Member 2 — Roommate Finder

### Files responsible for
```
ui/screens/roommate/
  RoommateFinderScreen.kt
  RoommateProfileScreen.kt
  RoommateViewModel.kt

ui/components/
  RoommateCard.kt

data/model/
  RoommateProfile.kt           ← hobbies, studyHabit, sleepPattern, smoking, budget

data/repository/
  RoommateRepository.kt        ← Firestore CRUD for profiles

domain/usecase/roommate/
  GetRoommateMatchesUseCase.kt ← weighted compatibility scoring algorithm
```

### Feature overview
- Students create a profile: hobbies (multi-select tags), study habit, sleep pattern, smoking/drinking, budget range, bio, privacy toggle
- `GetRoommateMatchesUseCase` scores all other visible profiles against the user's profile (0–100)
- `RoommateFinderScreen` shows a swipeable card stack sorted by match score
- "Looking for Roommate" toggle hides the user from other students' feeds

### Data flow
```
RoommateProfileScreen → RoommateViewModel.saveProfile(profile) →
RoommateRepository.saveProfile(profile) → Firestore roommate_profiles/{userId}

RoommateFinderScreen → RoommateViewModel.loadMatches() →
RoommateRepository.getProfiles(excludeMyId) → Firestore query →
GetRoommateMatchesUseCase.invoke(myProfile, candidates) → sorted list →
RoommateFinderScreen displays ranked RoommateCards
```

### Scoring algorithm (GetRoommateMatchesUseCase)
| Factor | Points |
|---|---|
| Same study habit | 20 |
| Same sleep pattern | 20 |
| Smoking preference match | 15 |
| Drinking preference match | 15 |
| Shared hobbies (5 pts each, max) | 20 |
| Budget range overlap | 10 |
| **Total max** | **100** |

### Likely viva questions
- Walk through the scoring algorithm. What are its limitations?
- How is privacy implemented? What Firestore query enforces it?
- How would you improve the matching algorithm?
- What happens when a student updates their profile — does the match score update in real time?

---

## Member 3 — Search & Filtering

### Files responsible for
```
ui/screens/listings/
  ListingsScreen.kt            ← advanced search UI (all filters expanded)

ui/screens/home/
  HomeScreen.kt                ← search bar + filter chips (quick filters)
  HomeViewModel.kt             ← search query, filter state, listings StateFlow

domain/usecase/listing/
  GetListingsUseCase.kt
  FilterListingsUseCase.kt     ← pure filter function (no network call)

data/local/dao/
  ListingDao.kt                ← SQL search and filter queries

data/repository/
  ListingRepository.kt         ← offline-first: Room cache + Firestore sync
```

### Feature overview
- **Quick filters** on HomeScreen: price slider, room type chips (Single/Shared/Annex), gender policy chips, distance from campus
- **Advanced search** on ListingsScreen: all filters plus amenities checklist, keyword search
- `FilterListingsUseCase` is a pure Kotlin function — no suspend, filters an in-memory list
- `ListingDao` stores cached listings locally so the feed loads instantly offline
- Distance is calculated using the Haversine formula (`util/Extensions.kt`) against SLIIT Malabe campus coordinates

### Data flow
```
HomeScreen typing →
HomeViewModel.searchQuery.value = query →
HomeViewModel.applyFilters() →
FilterListingsUseCase.invoke(allListings, query, maxPrice, type, gender, maxDistance, amenities) →
HomeViewModel._listings.value = filteredList →
LazyColumn recomposes
```

### Likely viva questions
- Why is `FilterListingsUseCase` not a suspend function?
- How does offline-first caching work with Room + Firestore?
- What is the Haversine formula and why is it used here?
- How do you prevent the filter from making a network call on every keystroke?
- What index would you add to Firestore to speed up listing queries?

---

## Member 4 — Booking Management

### Files responsible for
```
ui/screens/listings/
  ListingDetailScreen.kt       ← contact owner button, booking initiation
  PostListingScreen.kt         ← owner creates listing (multi-step wizard)
  ListingsViewModel.kt         ← post, edit, delete, review state

domain/usecase/listing/
  PostListingUseCase.kt        ← validate + upload photos + Firestore write

data/model/
  Listing.kt                   ← full listing model with isVerified flag
  Review.kt

data/repository/
  ListingRepository.kt         ← CRUD + reviews
```

### Feature overview
- **Owner** fills multi-step form: Photos → Details (title, price, type, gender) → Amenities checkboxes → Map pin (location picker) → Submit
- Photos uploaded to Firebase Storage; URLs stored in Listing.imageUrls
- Submitted listing has `isVerified = false`; hidden from student feed until admin approves
- **Student** on ListingDetailScreen can contact owner (opens Chat) or save listing
- Students can leave a star rating + written review after their stay

### Data flow
```
PostListingScreen → ListingsViewModel.submitListing() →
PostListingUseCase.invoke(listing, imageUris) →
Firebase Storage upload (images) → get download URLs →
Firestore listings/{id} write (isVerified = false) →
Admin approves (isVerified = true) →
Listing appears in student feed
```

### Likely viva questions
- How are listing photos uploaded and stored?
- Why is `isVerified` needed? What is the admin approval flow?
- How do you prevent duplicate listings from the same owner?
- What happens to a listing's images if the listing is deleted?
- How is the average rating calculated and stored?

---

## Member 5 — Payment Management

### Files responsible for
```
ui/screens/payment/
  RentTrackerScreen.kt         ← monthly ledger, payment history
  ReceiptUploadScreen.kt       ← image picker + Firebase Storage upload
  PaymentViewModel.kt

data/model/
  Payment.kt                   ← receiptUrl, receiptVerified, status
  SplitBill.kt / SplitParticipant

data/repository/
  PaymentRepository.kt         ← uploadReceipt, verifyReceipt, split bill CRUD
```

### Feature overview
- **No payment gateway** — all payments are made offline (bank transfer / cash)
- Tenant opens `ReceiptUploadScreen`, picks photo of payment slip, uploads to Firebase Storage
- `Payment.receiptUrl` is saved; `Payment.status = PENDING`
- Owner receives notification, views receipt, clicks "Confirm" → `receiptVerified = true`, `status = PAID`
- **Split-bill:** Student creates a bill (e.g. "Electricity — March"), adds housemates by email, system divides total; each person uploads their receipt share

### Data flow
```
Tenant: ReceiptUploadScreen →
PaymentViewModel.uploadReceipt(paymentId, imageUri) →
Firebase Storage upload → receiptUrl →
Firestore payments/{id}.receiptUrl = url, status = PENDING_VERIFICATION

Owner: notification → opens owner dashboard →
PaymentViewModel.verifyReceipt(paymentId) →
Firestore payments/{id}.receiptVerified = true, status = PAID
```

### Likely viva questions
- Why was a payment gateway removed in favour of receipt upload?
- How do you prevent fake receipt images?
- What Firestore security rules ensure only the owner can verify a receipt?
- How does split-bill work if one participant fails to pay?
- How would you add a payment gateway in a future sprint?

---

## Member 6 — Owner Dashboard & Owner-Student Communication

### Files responsible for
```
ui/screens/chat/
  ConversationsScreen.kt       ← list of all conversations
  ChatScreen.kt                ← real-time message bubbles + image sharing
  ChatViewModel.kt

data/model/
  ChatMessage.kt
  Conversation.kt

data/repository/
  ChatRepository.kt            ← Firestore snapshots, sendMessage, sendImage

domain/usecase/chat/
  GetMessagesUseCase.kt
  SendMessageUseCase.kt

ui/screens/profile/ (owner view)
  — owner sees their listings, pending receipts, incoming chat requests

Firebase Cloud Messaging setup (push notifications)
```

### Feature overview
- **Real-time chat** powered by Firestore `snapshots()` — messages update live without polling
- Students initiate a conversation from `ListingDetailScreen` ("Contact Owner")
- Both parties can send text and images (Firebase Storage for image upload)
- **Conversation list** shows last message, timestamp, unread count badge
- **Push notifications** via Firebase Cloud Messaging when a new message arrives while app is backgrounded
- Owner sees a receipt-verification queue in their dashboard (Sprint 6 integration)

### Data flow
```
Student taps "Contact Owner" →
ChatRepository.createConversation([studentId, ownerId], listingId) →
Firestore chats/{conversationId} created →
ChatScreen opens →
ChatViewModel.loadMessages(conversationId) →
Firestore chats/{id}/messages snapshots() → Flow<List<ChatMessage>> →
messages StateFlow → LazyColumn recomposes on every new message

Send message:
ChatViewModel.sendMessage() →
SendMessageUseCase.invoke(ChatMessage(...)) →
Firestore chats/{id}/messages add document →
FCM trigger (Cloud Function) → push to recipient device
```

### Likely viva questions
- How does Firestore `snapshots()` differ from a one-shot `get()` call?
- How do you mark messages as read and update the unread badge?
- How are images sent in chat? Where are they stored?
- How are push notifications triggered when the app is closed?
- What Firestore security rule ensures a user can only read conversations they are part of?
- How would you add typing indicators?

---

## Shared architecture questions (all members should know)

- **Why Hilt for DI?** Compile-time verification, integrates with ViewModel, reduces boilerplate.
- **Why Repository pattern?** Single source of truth; UI doesn't care whether data is from Room or Firestore.
- **Why StateFlow over LiveData?** Kotlin-first, cold-start safe, works well with Compose `collectAsState()`.
- **Why offline-first with Room?** App works without internet; Firestore syncs when online.
- **Glassmorphism implementation:** Frosted glass = semi-transparent white background + backdrop blur + 1px semi-transparent border. On Android: `RenderEffect.createBlurEffect()` (API 31+) with a fallback tint for older devices.
- **Navigation:** Single-activity with Navigation Compose. `NavGraph.kt` maps route strings to composables. Auth state determines `startDestination`.
