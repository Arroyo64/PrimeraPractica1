package com.example.primerapractica.domain.usecase.auth

import com.example.primerapractica.data.repository.AuthRepository

class LogoutUseCase(
    private val repo: AuthRepository = AuthRepository()
) {
    operator fun invoke() = repo.logout()
}