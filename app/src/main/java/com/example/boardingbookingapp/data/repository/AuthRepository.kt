package com.example.birdnest.data.repository

import com.example.birdnest.data.model.User
import com.example.birdnest.util.Result
import kotlinx.coroutines.flow.Flow

// Sprint 1 — implement with Firebase Auth + Firestore + Resend email API
interface AuthRepository {
    val currentUser: Flow<User?>

    // Student auth — email/password with @my.sliit.lk domain gate
    suspend fun sendOtp(email: String): Result<Unit>          // calls Resend API via Firebase Function
    suspend fun verifyOtp(email: String, otp: String): Result<User>
    suspend fun signInWithEmail(email: String, password: String): Result<User>

    // Owner auth — email/password + KYC
    suspend fun registerOwner(email: String, password: String): Result<User>
    suspend fun submitKyc(userId: String, nicImageUri: String, selfieUri: String): Result<Unit>

    suspend fun signOut()
    suspend fun createProfile(user: User): Result<User>
    suspend fun getProfile(userId: String): Result<User>
    fun isEmailValid(email: String): Boolean
}
