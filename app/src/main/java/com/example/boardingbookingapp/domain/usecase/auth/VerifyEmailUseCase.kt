package com.example.boardingbookingapp.domain.usecase.auth

import com.example.boardingbookingapp.data.repository.AuthRepository

// Sprint 1 — inject via Hilt
class VerifyEmailUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(email: String): Boolean = authRepository.isEmailValid(email)
}
