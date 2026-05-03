# Project Checklist

## Sprint 0 — Foundation
- [x] Create initial onboarding UI/UX flow
- [x] Implement Onboarding Screen with modern slides (Clean/Blue design)
- [x] Update NavGraph to handle onboarding redirect
- [x] Implement Browse Listings as default landing after onboarding
- [x] Add "Sign in to Book" logic for students
- [x] Update Login/OTP/Register UI to match modern design
- [x] Update all app screens (Home, Detail, Profile, Admin, etc.) to modern design

## Sprint 1 — Firebase + Dependency Injection
- [x] Add Hilt, KSP, Room, Retrofit, Firebase dependencies
- [x] Fix Hilt + AGP 9.2.0 compatibility (upgrade to Hilt 2.59.2)
- [x] Fix KSP + AGP 9.x source sets issue (gradle.properties flag)
- [x] Fix google-services.json package name mismatch
- [x] Set up DI modules (AppModule, DatabaseModule, NetworkModule, RepositoryModule)
- [x] Create Room entities (UserEntity, ListingEntity, ChatMessageEntity)
- [x] Create Room DAOs (ListingDao, UserDao, ChatDao)
- [x] Create AppDatabase
- [x] Implement FirebaseAuthRepository (Auth + Firestore)
- [x] Rename package from com.example.birdnest → com.example.boardingbookingapp

## Sprint 2 — Student Sign-Up Flow
- [x] Update User model (firstName, lastName, dateOfBirth, mobileNumber, gender, academicYear)
- [x] Add Gender enum (MALE, FEMALE, OTHER)
- [x] Add Resend API constants to Constants.kt
- [x] Create ResendApi Retrofit interface
- [x] Add @Named("resend") Retrofit instance in NetworkModule
- [x] Add registerStudent() to AuthRepository interface
- [x] Implement registerStudent() in FirebaseAuthRepository
- [x] Update sendOtp() → 4-digit OTP + send via Resend email
- [x] Update verifyOtp() → handles registration vs login flow
- [x] Add registerStudent() + isPasswordStrong() to AuthViewModel
- [x] Update verifyOtp() in AuthViewModel → emits Authenticated (not NeedsProfile)
- [x] Add Welcome, StudentRegister, StudentDashboard routes to Screen.kt
- [x] Create WelcomeScreen (landing: Create Account / Login / Owner)
- [x] Create StudentRegisterScreen (name, DOB+16yr check, mobile, gender, year, email, password strength)
- [x] Update OtpVerifyScreen: 6-digit → 4-digit
- [x] Create StudentDashboardScreen (6 action cards, no admin panel)
- [x] Remove Admin Panel from ProfileScreen
- [x] Update BottomNavBar Home tab → StudentDashboard
- [x] Wire NavGraph (Welcome, StudentRegister, StudentDashboard, fix OtpVerify flow)

## Sprint 3 — Student Dashboard Features
- [ ] Implement actual booking flow after student sign in
- [ ] Booking list screen for students
- [ ] Payment receipt upload (connect to Firebase Storage)
- [ ] Review submission (connect to Firestore)
- [ ] Support ticket submission (connect to Firestore)

## Sprint 4 — Owner Sign-Up Flow
- [x] Owner registration with KYC (email/password, no OTP)
- [x] Owner dashboard (action cards: Add Listing, My Listings, Messages, Browse, Profile)
- [x] Owner can post listings (Firestore save wired to OwnerViewModel)
- [x] Route OWNER role to OwnerDashboard after login (KYC-status aware routing)

## Sprint 5 — Admin Panel (separate)
- [ ] Admin dashboard (separate from student/owner flow)
- [ ] KYC review and approval
- [ ] User management

---

## Admin Dashboard Implementation

### Android App — Admin Routing & In-App Admin Screen
- [x] Add `navigateAfterLogin()` to NavGraph — routes ADMIN role to AdminDashboard, others to StudentDashboard
- [x] Create `AdminViewModel` with Firestore injection (real-time listener for platform reviews)
- [x] `AdminViewModel.approveReview(id)` — sets `isApproved = true` in Firestore
- [x] `AdminViewModel.deleteReview(id)` — deletes document from Firestore
- [x] Update `AdminDashboardScreen` — add Reviews tab with live Approve/Delete actions
- [x] Android: Admin can approve/reject owner KYC from in-app admin screen
- [x] Android: Admin can ban/unban users from in-app admin screen
- [x] Android: Admin can remove inappropriate listings from in-app admin screen

### Web Admin Panel (`admin-panel/index.html`)
- [x] Single HTML file — no build step, open directly in browser
- [x] Firebase JS SDK v10.12.0 (Auth + Firestore) via CDN
- [x] Tailwind CSS + Chart.js + Font Awesome via CDN
- [x] Login screen with Firebase Auth + Firestore role=ADMIN check
- [x] Fixed sidebar (Overview, Users, Listings, Reviews, KYC, Support) with badge counters
- [x] Confirmation modal (reusable, color-coded per action type)
- [x] Toast notifications (success / danger / primary / warning)
- [x] CSV export for current section data
- [x] Real-time Firestore `onSnapshot` listeners in all sections

#### Overview Section
- [x] 5 stat cards (Total Users, Active Listings, Pending KYC, Open Tickets, Platform Reviews)
- [x] 14-day registrations Chart.js line chart with gradient fill
- [x] Recent sign-ups list
- [x] Role breakdown (Students vs Owners)

#### Users Section
- [x] Search by name/email
- [x] Filter by role and status (active/banned)
- [x] Table with user details
- [x] Ban / Unban action (with confirmation modal)
- [x] View full user profile details from admin panel
- [x] Bulk ban/export selected users

#### Listings Section
- [x] Table with listing title, owner, price, status
- [x] Approve listing action
- [x] Remove inappropriate listing (with confirmation modal)
- [x] Preview listing details in a modal
- [x] Filter listings by status (pending / approved / removed)

#### Reviews Section
- [x] Grid cards showing pending reviews (Approve / Delete)
- [x] Grid cards showing approved reviews (Delete)
- [x] Real-time updates via Firestore snapshot
- [x] Filter reviews by rating
- [x] Reply/note field for admin on reviews

#### KYC Section
- [x] Cards for owners with PENDING_REVIEW status
- [x] NIC/selfie document links
- [x] Approve KYC action (sets status to APPROVED in Firestore)
- [x] Reject KYC action (sets status to REJECTED in Firestore)
- [x] Show rejection reason input before rejecting
- [x] Notify owner via Firestore flag when KYC is approved/rejected

#### Support Tickets Section
- [x] Table with ticket title, student, status, created date
- [x] Status flow: Open → In Progress → Resolved → Delete
- [x] View full ticket message in a modal
- [x] Admin reply/note on ticket stored in Firestore

### Setup Steps (Manual — User Must Do)
- [x] Go to Firebase Console → Project Settings → Your apps → Add web app → copy `appId`
- [x] Paste `appId` into `admin-panel/index.html` replacing `"PASTE_YOUR_WEB_APP_ID_HERE"`
- [x] Create admin account: Firebase Auth → Add user (e.g. `admin@birdnest.lk` + password)
- [ ] Create Firestiore document: `users/{uid}` with `role: "ADMIN"`, `displayName: "Admin"`, `isActive: true`, `email: <email>`, `id: <uid>`, `createdAt: <timestamp>`
- [ ] Update Firestore security rules to allow `platform_reviews` read/write for authenticated users
- [ ] Open `admin-panel/index.html` in Chrome/Edge and log in with admin credentials
