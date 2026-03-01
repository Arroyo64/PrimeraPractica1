package com.example.primerapractica.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.primerapractica.presentation.navigation.NavScreen
import com.example.primerapractica.presentation.ui.components.GameCard
import com.example.primerapractica.presentation.viewmodel.AuthViewModel
import com.example.primerapractica.presentation.viewmodel.GamesState
import com.example.primerapractica.presentation.viewmodel.GamesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    nav: NavController,
    authVm: AuthViewModel = koinViewModel(),
    gamesVm: GamesViewModel = koinViewModel()
) {
    val uid = authVm.uidOrNull() ?: ""

    val games by gamesVm.games.collectAsState()
    val state by gamesVm.state.collectAsState()

    LaunchedEffect(uid) {
        if (uid.isNotBlank()) gamesVm.load(uid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Biblioteca de videojuegos") },
                actions = {
                    IconButton(onClick = {
                        authVm.logout()
                        nav.navigate(NavScreen.Login.route) {
                            popUpTo(NavScreen.Home.route) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate(NavScreen.AddGame.route) }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->

        when (state) {
            is GamesState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(padding))
            }

            is GamesState.Error -> {
                Text(
                    text = (state as GamesState.Error).message,
                    modifier = Modifier.padding(padding)
                )
            }

            else -> {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(games, key = { it.id }) { game ->
                        GameCard(
                            game = game,
                            onEdit = { nav.navigate(NavScreen.EditGame.createRoute(game.id)) },
                            onDelete = { if (uid.isNotBlank()) gamesVm.delete(uid, game.id) }
                        )
                    }
                }
            }
        }
    }
}