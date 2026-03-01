package com.example.primerapractica.di

import com.example.primerapractica.data.repository.AuthRepository
import com.example.primerapractica.data.repository.GamesRepository
import com.example.primerapractica.domain.usecase.auth.GetCurrentUidUseCase
import com.example.primerapractica.domain.usecase.auth.LoginUserUseCase
import com.example.primerapractica.domain.usecase.auth.LogoutUseCase
import com.example.primerapractica.domain.usecase.auth.RegisterUserUseCase
import com.example.primerapractica.domain.usecase.games.AddGameUseCase
import com.example.primerapractica.domain.usecase.games.DeleteGameUseCase
import com.example.primerapractica.domain.usecase.games.GetGamesUseCase
import com.example.primerapractica.domain.usecase.games.UpdateGameUseCase
import com.example.primerapractica.presentation.viewmodel.AuthViewModel
import com.example.primerapractica.presentation.viewmodel.GamesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Repos
    single { AuthRepository() }
    single { GamesRepository() }

    // UseCases auth
    factory { LoginUserUseCase(get()) }
    factory { RegisterUserUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUidUseCase(get()) }

    // UseCases games
    factory { GetGamesUseCase(get()) }
    factory { AddGameUseCase(get()) }
    factory { UpdateGameUseCase(get()) }
    factory { DeleteGameUseCase(get()) }

    // ViewModels
    viewModel { AuthViewModel(get(), get(), get(), get()) }
    viewModel { GamesViewModel(get(), get(), get(), get()) }
}