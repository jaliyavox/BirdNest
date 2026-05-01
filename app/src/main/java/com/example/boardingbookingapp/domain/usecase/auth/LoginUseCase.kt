package com.example.birdnest.domain.usecase.auth

import com.example.birdnest.data.model.User
import com.example.birdnest.data.repository.AuthRepository
import com.example.birdnest.util.Result

// Sprint 1 — inject via Hilt after adding Hilt dependency
class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        authRepository.signInWithEmail(email, password)
}
