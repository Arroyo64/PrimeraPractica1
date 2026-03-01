package com.example.primerapractica.domain.usecase.games

import com.example.primerapractica.data.repository.GamesRepository
import com.example.primerapractica.domain.model.Game

class UpdateGameUseCase(private val repo: GamesRepository) {
    suspend operator fun invoke(uid: String, game: Game) = repo.update(uid, game)
}