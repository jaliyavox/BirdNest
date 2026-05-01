package com.example.birdnest.data.remote.api

import com.example.birdnest.data.remote.dto.UserDto

// Sprint 1 — add Retrofit @GET/@POST annotations after adding Retrofit dependency
interface AuthApi {
    suspend fun getProfile(userId: String): UserDto
    suspend fun createProfile(dto: UserDto): UserDto
    suspend fun updateProfile(userId: String, dto: UserDto): UserDto
}
