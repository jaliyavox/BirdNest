package com.example.birdnest.data.remote.dto

import com.example.birdnest.data.model.User
import com.example.birdnest.data.model.UserRole

data class UserDto(
    val id: String = "",
    val email: String = "",
    val display_name: String = "",
    val photo_url: String = "",
    val role: String = "STUDENT",
    val is_verified: Boolean = false,
)

fun UserDto.toDomain() = User(
    id = id,
    email = email,
    displayName = display_name,
    photoUrl = photo_url,
    role = runCatching { UserRole.valueOf(role) }.getOrDefault(UserRole.STUDENT),
    isEmailVerified = is_verified,
)
