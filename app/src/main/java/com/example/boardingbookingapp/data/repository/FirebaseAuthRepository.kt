package com.example.boardingbookingapp.data.repository

import com.example.boardingbookingapp.data.model.KycStatus
import com.example.boardingbookingapp.data.model.User
import com.example.boardingbookingapp.data.model.UserRole
import com.example.boardingbookingapp.util.Constants
import com.example.boardingbookingapp.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val uid = firebaseAuth.currentUser?.uid
            if (uid == null) {
                trySend(null)
            } else {
                firestore.collection(Constants.COLLECTION_USERS).document(uid)
                    .get()
                    .addOnSuccessListener { doc ->
                        trySend(doc.toObject(User::class.java))
                    }
                    .addOnFailureListener { trySend(null) }
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    // OTP flow: stores a generated code in Firestore; a Cloud Function (Resend) sends the email.
    // Until the Cloud Function is deployed, any 6-digit code is accepted in verifyOtp().
    override suspend fun sendOtp(email: String): Result<Unit> {
        if (!isEmailValid(email)) return Result.Error("Only @my.sliit.lk emails are allowed")
        return try {
            val otp = (100000..999999).random().toString()
            firestore.collection("otp_requests")
                .document(email.lowercase())
                .set(mapOf("otp" to otp, "createdAt" to System.currentTimeMillis()))
                .await()
            // TODO: Cloud Function picks this up and sends via Resend API
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to send OTP")
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<User> {
        return try {
            val doc = firestore.collection("otp_requests")
                .document(email.lowercase())
                .get().await()
            val storedOtp = doc.getString("otp") ?: ""
            val createdAt = doc.getLong("createdAt") ?: 0L
            val expired = System.currentTimeMillis() - createdAt > 10 * 60 * 1000 // 10 min
            if (otp != storedOtp || expired) {
                return Result.Error("Invalid or expired OTP")
            }
            // Sign in via email link auth or anonymous + custom claim; use email/password workaround
            val password = "otp_${otp}_${email.lowercase().hashCode()}"
            val result = try {
                auth.signInWithEmailAndPassword(email, password).await()
            } catch (_: Exception) {
                auth.createUserWithEmailAndPassword(email, password).await()
            }
            val uid = result.user?.uid ?: return Result.Error("Auth failed")
            val user = User(id = uid, email = email, role = UserRole.STUDENT, isEmailVerified = true)
            firestore.collection(Constants.COLLECTION_USERS).document(uid).set(user).await()
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Verification failed")
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.Error("Sign-in failed")
            val doc = firestore.collection(Constants.COLLECTION_USERS).document(uid).get().await()
            val user = doc.toObject(User::class.java) ?: return Result.Error("Profile not found")
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Sign-in failed")
        }
    }

    override suspend fun registerOwner(email: String, password: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.Error("Registration failed")
            val user = User(
                id = uid,
                email = email,
                role = UserRole.OWNER,
                kycStatus = KycStatus.NOT_SUBMITTED,
            )
            firestore.collection(Constants.COLLECTION_USERS).document(uid).set(user).await()
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Registration failed")
        }
    }

    override suspend fun submitKyc(userId: String, nicImageUri: String, selfieUri: String): Result<Unit> = try {
        firestore.collection(Constants.COLLECTION_USERS).document(userId)
            .update(
                mapOf(
                    "kycStatus" to KycStatus.PENDING_REVIEW.name,
                    "nicImageUrl" to nicImageUri,
                    "selfieUrl" to selfieUri,
                )
            ).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message ?: "KYC submission failed")
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun createProfile(user: User): Result<User> = try {
        firestore.collection(Constants.COLLECTION_USERS).document(user.id).set(user).await()
        Result.Success(user)
    } catch (e: Exception) {
        Result.Error(e.message ?: "Failed to save profile")
    }

    override suspend fun getProfile(userId: String): Result<User> {
        return try {
            val doc = firestore.collection(Constants.COLLECTION_USERS).document(userId).get().await()
            val user = doc.toObject(User::class.java) ?: return Result.Error("User not found")
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch profile")
        }
    }

    override fun isEmailValid(email: String): Boolean =
        email.endsWith(Constants.SLIIT_EMAIL_DOMAIN, ignoreCase = true)
}
