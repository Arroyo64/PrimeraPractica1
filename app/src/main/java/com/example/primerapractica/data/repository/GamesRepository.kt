package com.example.primerapractica.data.repository

import com.example.primerapractica.data.FirebaseProvider
import com.example.primerapractica.domain.model.Game
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class GamesRepository {

    private val db = FirebaseProvider.db

    private fun ref(uid: String) =
        db.collection("users").document(uid).collection("games")

    suspend fun list(uid: String): List<Game> {
        val snap = ref(uid).orderBy("createdAt").get().await()
        return snap.documents.mapNotNull { it.toGameOrNull() }
    }

    suspend fun add(uid: String, game: Game) {
        val doc = ref(uid).document()
        doc.set(
            mapOf(
                "title" to game.title,
                "platform" to game.platform,
                "genre" to game.genre,
                "status" to game.status,
                "notes" to game.notes,
                "rating" to game.rating,
                "hoursPlayed" to game.hoursPlayed,
                "finishedDate" to game.finishedDate,
                "createdAt" to game.createdAt
            )
        ).await()
    }

    suspend fun update(uid: String, game: Game) {
        ref(uid).document(game.id).update(
            mapOf(
                "title" to game.title,
                "platform" to game.platform,
                "genre" to game.genre,
                "status" to game.status,
                "notes" to game.notes,
                "rating" to game.rating,
                "hoursPlayed" to game.hoursPlayed,
                "finishedDate" to game.finishedDate
            )
        ).await()
    }

    suspend fun delete(uid: String, id: String) {
        ref(uid).document(id).delete().await()
    }

    private fun DocumentSnapshot.toGameOrNull(): Game? {
        val title = getString("title") ?: return null
        val platform = getString("platform") ?: ""
        val genre = getString("genre") ?: ""
        val status = getString("status") ?: "Pendiente"
        val notes = getString("notes") ?: ""
        val rating = (getLong("rating") ?: 0L).toInt()
        val hours = getDouble("hoursPlayed") ?: (getLong("hoursPlayed")?.toDouble() ?: 0.0)
        val finished = getString("finishedDate") ?: ""
        val createdAt = getLong("createdAt") ?: 0L

        return Game(
            id = id,
            title = title,
            platform = platform,
            genre = genre,
            status = status,
            notes = notes,
            rating = rating,
            hoursPlayed = hours,
            finishedDate = finished,
            createdAt = createdAt
        )
    }
}