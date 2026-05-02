package com.example.boardingbookingapp.data.repository

import android.util.Log
import com.example.boardingbookingapp.data.model.Gender
import com.example.boardingbookingapp.data.model.KycStatus
import com.example.boardingbookingapp.data.model.User
import com.example.boardingbookingapp.data.model.UserRole
import com.example.boardingbookingapp.util.Constants
import com.example.boardingbookingapp.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AuthRepo"
private const val FIRESTORE_TIMEOUT_MS = 15_000L

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
                    .addOnSuccessListener { doc -> trySend(doc.toObject(User::class.java)) }
                    .addOnFailureListener { trySend(null) }
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun registerStudent(
        email: String, password: String,
        firstName: String, lastName: String,
        dateOfBirth: Long, mobileNumber: String,
        gender: Gender, academicYear: Int,
    ): Result<User> {
        return try {
            Log.d(TAG, "registerStudent: creating auth user for $email")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.Error("Registration failed")
            Log.d(TAG, "registerStudent: auth user created, uid=$uid — writing Firestore profile")
            val user = User(
                id = uid, email = email,
                firstName = firstName, lastName = lastName,
                displayName = "$firstName $lastName",
                dateOfBirth = dateOfBirth,
                mobileNumber = mobileNumber,
                gender = gender,
                academicYear = academicYear,
                role = UserRole.STUDENT,
                createdAt = System.currentTimeMillis(),
            )
            withTimeout(FIRESTORE_TIMEOUT_MS) {
                firestore.collection(Constants.COLLECTION_USERS).document(uid).set(user).await()
            }
            Log.d(TAG, "registerStudent: Firestore profile written for uid=$uid")
            Result.Success(user)
        } catch (e: CancellationException) {
            throw e
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "registerStudent: Firestore write timed out — check Firestore rules / network", e)
            Result.Error("Firestore write timed out. Check your Firestore security rules — they may be blocking writes for authenticated users.")
        } catch (e: Exception) {
            Log.e(TAG, "registerStudent: failed", e)
            Result.Error(e.message ?: "Registration failed")
        }
    }

    override suspend fun sendOtp(email: String): Result<Unit> =
        Result.Error("OTP login not supported")

    override suspend fun verifyOtp(email: String, otp: String): Result<User> =
        Result.Error("OTP login not supported")

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
                id = uid, email = email,
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
            .update(mapOf(
                "kycStatus"   to KycStatus.PENDING_REVIEW.name,
                "nicImageUrl" to nicImageUri,
                "selfieUrl"   to selfieUri,
            )).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message ?: "KYC submission failed")
    }

    override suspend fun signOut() { auth.signOut() }

    override suspend fun createProfile(user: User): Result<User> = try {
        firestore.collection(Constants.COLLECTION_USERS).document(user.id).set(user).await()
        Result.Success(user)
    } catch (e: Exception) {
        Result.Error(e.message ?: "Failed to save profile")
    }

    override suspend fun getProfile(userId: String): Result<User> {
        return try {
            val doc  = firestore.collection(Constants.COLLECTION_USERS).document(userId).get().await()
            val user = doc.toObject(User::class.java) ?: return Result.Error("User not found")
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch profile")
        }
    }

    override fun isEmailValid(email: String): Boolean =
        email.endsWith(Constants.SLIIT_EMAIL_DOMAIN, ignoreCase = true)
}
