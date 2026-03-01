package com.example.primerapractica.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.primerapractica.domain.model.Game

@Composable
fun GameCard(
    game: Game,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(game.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("${game.platform} · ${game.status}", style = MaterialTheme.typography.bodyMedium)

            if (expanded) {
                Spacer(Modifier.height(10.dp))
                Text("Género: ${game.genre}")
                Text("Nota: ${game.rating}/10")
                Text("Horas jugadas: ${game.hoursPlayed}")

                if (game.finishedDate.isNotBlank()) {
                    Text("Fecha de finalización: ${game.finishedDate}")
                }

                if (game.notes.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(game.notes)
                }

                Spacer(Modifier.height(12.dp))

                Row {
                    FilledTonalIconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                    }
                    Spacer(Modifier.width(8.dp))
                    FilledTonalIconButton(onClick = { confirmDelete = true }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Eliminar videojuego") },
            text = { Text("¿Seguro que quieres eliminar “${game.title}”?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmDelete = false
                    onDelete()
                }) { Text("Sí") }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("No") }
            }
        )
    }
}