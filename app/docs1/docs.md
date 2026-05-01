# Prompt for SLIIT Nest: Housing & Roommate Finder App

**Instruction for the AI:**
Act as a Senior Full-Stack Developer and UI/UX Architect. Your task is to generate a comprehensive project structure, technical roadmap, and design system for a mobile application called **"SLIIT Nest"**. This app is specifically designed for students at SLIIT University.

---

## 1. Project Brief
**Target Audience:** SLIIT University Students (Verified via `@my.sliit.lk` email).
**Primary Goal:** To bridge the gap between students looking for accommodation and landlords/owners in the Malabe/Kaduwela area, while facilitating peer-to-peer roommate matching.

---

## 2. Core Functional Requirements

### A. Authentication & Verification
* **Student Login:** OAuth2 or Magic Link using SLIIT student email domain.
* **Owner Login:** Phone/NIC verification for landlords.
* **Role-Based Access:** Distinct interfaces for Students, Owners, and Super Admins.

### B. Smart Search & Filtering
* **Location-Based:** Distance from SLIIT Malabe Campus or Metro Campus.
* **Infrastructure Filters:** Proximity to shuttle routes, supermarkets, and gyms.
* **Property Filters:** Price range, gender-specific (Girls/Boys/Mixed), room type (Single/Shared), and amenities (AC, Wi-Fi, Kitchen access).

### C. Roommate Finder (Social Module)
* **Student Profiles:** Hobby-based tagging, study habits, sleeping patterns, and smoking/alcohol preferences.
* **Matching Algorithm:** Suggest potential roommates based on shared preferences and budget.
* **Privacy:** Option to toggle visibility of the "Looking for Roommate" status.

### D. Communication & Payment
* **In-App Messaging:** Real-time chat between students and owners with image sharing for room inspections.
* **Rent Tracker:** Digital ledger for monthly rent payments.
* **Payment Gateway:** Integration for deposits and monthly fees (e.g., PayHere or FriMi API).

### E. Admin Control Panel
* **Moderation:** Verify listings before they go live.
* **Dispute Resolution:** Handle reports regarding bad tenants or fraudulent owners.
* **Analytics:** Track demand trends and average pricing in specific areas.

---

## 3. UI/UX Design Specifications
* **Style:** **Glassmorphism (Frosted Glass).**
* **Visual Guidelines:**
    * Use transparent, blurred background containers (`backdrop-filter: blur()`).
    * Subtle multi-color gradient backgrounds (Deep Blue, Violet, and Cyan accents).
    * Thin, semi-transparent white borders (1px) to define edges.
    * Vivid iconography and high-legibility typography (Inter or Poppins).
* **Interactions:** Smooth transitions, haptic feedback, and intuitive navigation.

---

## 4. Technical Deliverables Requested
1.  **System Architecture:** Diagram/Description of the Frontend, Backend, and Database interaction.
2.  **Database Schema:** Key tables (Users, Listings, Chats, Payments, RoommateMatches).
3.  **API Strategy:** REST or GraphQL endpoints for core features.
4.  **Security Measures:** How to handle data privacy and prevent unauthorized access to the student-only sections.
5.  **Implementation Roadmap:** Phase-wise development plan (MVP to Full Release).

---

## 5. Additional Suggestions for the Model
* Include a **"Shuttle Tracker"** integration or proximity indicator.
* Propose a **"Split-Bill"** feature for utilities (Water/Electricity).
* Suggest a **Rating/Review system** specifically for student feedback on boarding conditions.
