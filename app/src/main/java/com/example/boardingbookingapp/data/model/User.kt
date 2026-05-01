package com.example.birdnest.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val role: UserRole = UserRole.STUDENT,
    val isEmailVerified: Boolean = false,   // @my.sliit.lk OTP confirmed (students)
    val isKycVerified: Boolean = false,      // admin approved NIC (owners)
    val kycStatus: KycStatus = KycStatus.NOT_SUBMITTED,
    val phone: String = "",
    val nic: String = "",
    val nicImageUrl: String = "",
    val selfieUrl: String = "",
    val isActive: Boolean = true,            // admin can deactivate accounts
    val createdAt: Long = 0L,
)

enum class UserRole { STUDENT, OWNER, ADMIN }
enum class KycStatus { NOT_SUBMITTED, PENDING_REVIEW, APPROVED, REJECTED }
