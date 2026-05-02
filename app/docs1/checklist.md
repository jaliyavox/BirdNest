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
- [ ] (Ask before starting) Owner registration with KYC
- [ ] Owner dashboard

## Sprint 5 — Admin Panel (separate)
- [ ] Admin dashboard (separate from student/owner flow)
- [ ] KYC review and approval
- [ ] User management
