package com.example.primerapractica.domain.usecase.games

import com.example.primerapractica.data.repository.GamesRepository
import com.example.primerapractica.domain.model.Game

class GetGamesUseCase(private val repo: GamesRepository) {
    suspend operator fun invoke(uid: String): List<Game> = repo.list(uid)
}