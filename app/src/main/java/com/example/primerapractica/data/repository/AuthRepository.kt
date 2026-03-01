package com.example.primerapractica.data.repository

import com.example.primerapractica.data.FirebaseProvider
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseProvider.auth

    val currentUser = auth.currentUser
    fun uidOrNull(): String? = auth.currentUser?.uid

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun logout() {
        auth.signOut()
    }
}