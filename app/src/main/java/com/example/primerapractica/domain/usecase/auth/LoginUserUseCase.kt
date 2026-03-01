package com.example.primerapractica.domain.usecase.auth

import com.example.primerapractica.data.repository.AuthRepository

class LoginUserUseCase(
    private val repo: AuthRepository = AuthRepository()
) {
    suspend operator fun invoke(email: String, password: String) {
        repo.login(email.trim(), password)
    }
}