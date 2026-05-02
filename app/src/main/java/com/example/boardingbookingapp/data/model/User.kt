package com.example.boardingbookingapp.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: Long = 0L,
    val mobileNumber: String = "",
    val gender: Gender = Gender.MALE,
    val academicYear: Int = 1,
    val photoUrl: String = "",
    val role: UserRole = UserRole.STUDENT,
    val isEmailVerified: Boolean = false,
    val isKycVerified: Boolean = false,
    val kycStatus: KycStatus = KycStatus.NOT_SUBMITTED,
    val phone: String = "",
    val nic: String = "",
    val nicImageUrl: String = "",
    val selfieUrl: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
)

enum class UserRole { STUDENT, OWNER, ADMIN }
enum class KycStatus { NOT_SUBMITTED, PENDING_REVIEW, APPROVED, REJECTED }
enum class Gender { MALE, FEMALE, OTHER }
