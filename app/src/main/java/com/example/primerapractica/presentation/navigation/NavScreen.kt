package com.example.primerapractica.presentation.navigation

sealed class NavScreen(val route: String) {
    data object Login : NavScreen("login")
    data object Register : NavScreen("register")
    data object Home : NavScreen("home")
    data object AddGame : NavScreen("games/add")

    data object EditGame : NavScreen("games/{id}") {
        fun createRoute(id: String) = "games/$id"
    }
}