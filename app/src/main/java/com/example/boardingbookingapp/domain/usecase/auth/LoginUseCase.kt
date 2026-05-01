package com.example.boardingbookingapp.domain.usecase.auth

import com.example.boardingbookingapp.data.model.User
import com.example.boardingbookingapp.data.repository.AuthRepository
import com.example.boardingbookingapp.util.Result

// Sprint 1 — inject via Hilt after adding Hilt dependency
class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        authRepository.signInWithEmail(email, password)
}
