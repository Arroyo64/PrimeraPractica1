package com.example.primerapractica.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.primerapractica.presentation.ui.screens.*
import com.example.primerapractica.presentation.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {

    val nav = rememberNavController()

    // Solo para decidir startDestination
    val authVm: AuthViewModel = koinViewModel()
    val start = if (authVm.isLoggedIn()) NavScreen.Home.route else NavScreen.Login.route

    NavHost(navController = nav, startDestination = start) {

        composable(NavScreen.Login.route) {
            LoginScreen(navController = nav)
        }

        composable(NavScreen.Register.route) {
            RegisterScreen(navController = nav)
        }

        composable(NavScreen.Home.route) {
            MainScreen(nav = nav) // Ajusta MainScreen para que use koinViewModel internamente
        }

        composable(NavScreen.AddGame.route) {
            AddGameScreen(nav = nav) // Ajusta para que use koinViewModel
        }

        composable(NavScreen.EditGame.route) { backStack ->
            val id = backStack.arguments?.getString("id") ?: ""
            EditGameScreen(nav = nav, id = id) // Ajusta para que use koinViewModel
        }
    }
}