package com.example.primerapractica.domain.model

data class Game(
    val id: String,
    val title: String,
    val platform: String,
    val genre: String,
    val status: String,
    val notes: String,
    val rating: Int,
    val hoursPlayed: Double,
    val finishedDate: String,
    val createdAt: Long = System.currentTimeMillis()
)