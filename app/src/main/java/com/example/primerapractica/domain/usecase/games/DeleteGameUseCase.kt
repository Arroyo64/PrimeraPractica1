package com.example.primerapractica.domain.usecase.games

import com.example.primerapractica.data.repository.GamesRepository

class DeleteGameUseCase(private val repo: GamesRepository) {
    suspend operator fun invoke(uid: String, id: String) = repo.delete(uid, id)
}