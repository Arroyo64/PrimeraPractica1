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
fun AddGameScreen(
    nav: NavController,
    authVm: AuthViewModel = koinViewModel(),
    gamesVm: GamesViewModel = koinViewModel()
) {
    val uid = authVm.uidOrNull() ?: ""

    val state by gamesVm.state.collectAsState()

    var title by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var ratingText by remember { mutableStateOf("") }
    var hoursPlayedText by remember { mutableStateOf("") }
    var finishedDate by remember { mutableStateOf("") } // YYYY-MM-DD

    val statusOptions = listOf("Pendiente", "Jugando", "Completado", "Abandonado")
    var status by remember { mutableStateOf(statusOptions.first()) }
    var statusExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Escuchar evento de guardado OK
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
        topBar = { TopAppBar(title = { Text("Añadir videojuego") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = platform,
                onValueChange = { platform = it },
                label = { Text("Plataforma (PC, PS5, Switch...)") },
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
                supportingText = { Text("Ej: 12.5") },
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
                    val game = Game(
                        id = "", // Firestore crea ID
                        title = title.trim(),
                        platform = platform.trim(),
                        genre = genre.trim(),
                        status = status,
                        notes = notes.trim(),
                        rating = rating.coerceIn(0, 10),
                        hoursPlayed = hours.coerceAtLeast(0.0),
                        finishedDate = finishedDate.trim()
                    )
                    gamesVm.add(uid, game)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Guardando..." else "Guardar")
            }
        }
    }
}