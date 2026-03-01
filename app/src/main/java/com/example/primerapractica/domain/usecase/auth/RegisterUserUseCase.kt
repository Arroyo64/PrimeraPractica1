package com.example.primerapractica.domain.usecase.auth

import com.example.primerapractica.data.repository.AuthRepository

class RegisterUserUseCase(
    private val repo: AuthRepository = AuthRepository()
) {
    suspend operator fun invoke(email: String, password: String) {
        repo.register(email.trim(), password)
    }
}