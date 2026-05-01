package com.example.boardingbookingapp.data.repository

import com.example.boardingbookingapp.data.model.RoommateProfile
import com.example.boardingbookingapp.util.Result
import kotlinx.coroutines.flow.Flow

// Sprint 4 — implement with Firestore
interface RoommateRepository {
    suspend fun getMyProfile(userId: String): Result<RoommateProfile?>
    suspend fun saveProfile(profile: RoommateProfile): Result<RoommateProfile>
    fun getProfiles(excludeUserId: String): Flow<List<RoommateProfile>>
    suspend fun toggleVisibility(userId: String, visible: Boolean): Result<Unit>
}
