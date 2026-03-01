package com.example.primerapractica.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.primerapractica.domain.model.Game
import com.example.primerapractica.presentation.viewmodel.AuthViewModel
import com.example.primerapractica.presentation.viewmodel.GamesEvent
import com.example.primerapractica.presentation.viewmodel.GamesState
import com.example.primerapractica.presentation.viewmodel.GamesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGameScreen(
    nav: NavController,
    id: String,
    authVm: AuthViewModel = koinViewModel(),
    gamesVm: GamesViewModel = koinViewModel()
) {
    val uid = authVm.uidOrNull() ?: ""

    val games by gamesVm.games.collectAsState()
    val state by gamesVm.state.collectAsState()

    // Si entras directo a editar, asegúrate de cargar lista
    LaunchedEffect(uid) {
        if (uid.isNotBlank() && games.isEmpty()) gamesVm.load(uid)
    }

    val game = games.firstOrNull { it.id == id }

    var title by remember(game) { mutableStateOf(game?.title ?: "") }
    var platform by remember(game) { mutableStateOf(game?.platform ?: "") }
    var genre by remember(game) { mutableStateOf(game?.genre ?: "") }
    var notes by remember(game) { mutableStateOf(game?.notes ?: "") }
    var ratingText by remember(game) { mutableStateOf((game?.rating ?: 0).toString()) }
    var hoursPlayedText by remember(game) { mutableStateOf((game?.hoursPlayed ?: 0.0).toString()) }
    var finishedDate by remember(game) { mutableStateOf(game?.finishedDate ?: "") }

    val statusOptions = listOf("Pendiente", "Jugando", "Completado", "Abandonado")
    var status by remember(game) { mutableStateOf(game?.status ?: statusOptions.first()) }
    var statusExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Guardado OK -> volver atrás
    LaunchedEffect(Unit) {
        gamesVm.events.collect { ev ->
            when (ev) {
                GamesEvent.SavedOk -> nav.popBackStack()
            }
        }
    }

    // Errores
    LaunchedEffect(state) {
        if (state is GamesState.Error) {
            snackbarHostState.showSnackbar((state as GamesState.Error).message)
            gamesVm.clearError()
        }
    }

    val loading = state is GamesState.Loading

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar videojuego") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (game == null) {
                Text("Juego no encontrado")
                return@Column
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = platform,
                onValueChange = { platform = it },
                label = { Text("Plataforma") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Género") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded }
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    statusOptions.forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(opt) },
                            onClick = {
                                status = opt
                                statusExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = ratingText,
                onValueChange = { ratingText = it.filter { c -> c.isDigit() } },
                label = { Text("Nota (0-10)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = hoursPlayedText,
                onValueChange = { hoursPlayedText = it.replace(",", ".") },
                label = { Text("Horas jugadas") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = finishedDate,
                onValueChange = { finishedDate = it },
                label = { Text("Fecha de finalización (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notas") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            val rating = ratingText.toIntOrNull() ?: 0
            val hours = hoursPlayedText.toDoubleOrNull() ?: 0.0

            val enabled = uid.isNotBlank() && title.isNotBlank() && !loading

            Button(
                enabled = enabled,
                onClick = {
                    val updated = Game(
                        id = id,
                        title = title.trim(),
                        platform = platform.trim(),
                        genre = genre.trim(),
                        status = status,
                        notes = notes.trim(),
                        rating = rating.coerceIn(0, 10),
                        hoursPlayed = hours.coerceAtLeast(0.0),
                        finishedDate = finishedDate.trim(),
                        createdAt = game.createdAt
                    )
                    gamesVm.update(uid, updated)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Guardando..." else "Guardar cambios")
            }
        }
    }
}