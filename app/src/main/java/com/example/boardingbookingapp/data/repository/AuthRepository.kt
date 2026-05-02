package com.example.boardingbookingapp.data.repository

import com.example.boardingbookingapp.data.model.User
import com.example.boardingbookingapp.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>

    suspend fun registerStudent(
        email: String, password: String,
        firstName: String, lastName: String,
        dateOfBirth: Long, mobileNumber: String,
        gender: com.example.boardingbookingapp.data.model.Gender,
        academicYear: Int,
    ): Result<User>

    suspend fun sendOtp(email: String): Result<Unit>
    suspend fun verifyOtp(email: String, otp: String): Result<User>
    suspend fun signInWithEmail(email: String, password: String): Result<User>

    suspend fun registerOwner(email: String, password: String): Result<User>
    suspend fun submitKyc(userId: String, nicImageUri: String, selfieUri: String): Result<Unit>

    suspend fun signOut()
    suspend fun createProfile(user: User): Result<User>
    suspend fun getProfile(userId: String): Result<User>
    fun isEmailValid(email: String): Boolean
}
