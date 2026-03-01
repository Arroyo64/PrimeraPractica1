package com.example.primerapractica.domain.usecase.auth

import com.example.primerapractica.data.repository.AuthRepository

class GetCurrentUidUseCase(
    private val repo: AuthRepository = AuthRepository()
) {
    operator fun invoke(): String? = repo.uidOrNull()
}