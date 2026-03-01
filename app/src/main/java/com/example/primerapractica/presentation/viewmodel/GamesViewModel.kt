package com.example.primerapractica.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primerapractica.domain.model.Game
import com.example.primerapractica.domain.usecase.games.AddGameUseCase
import com.example.primerapractica.domain.usecase.games.DeleteGameUseCase
import com.example.primerapractica.domain.usecase.games.GetGamesUseCase
import com.example.primerapractica.domain.usecase.games.UpdateGameUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class GamesEvent {
    data object SavedOk : GamesEvent()
}

sealed class GamesState {
    data object Idle : GamesState()
    data object Loading : GamesState()
    data class Error(val message: String) : GamesState()
}

class GamesViewModel(
    private val getGames: GetGamesUseCase,
    private val addGame: AddGameUseCase,
    private val updateGame: UpdateGameUseCase,
    private val deleteGame: DeleteGameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<GamesState>(GamesState.Idle)
    val state: StateFlow<GamesState> = _state

    private val _events = MutableSharedFlow<GamesEvent>()
    val events = _events.asSharedFlow()

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _games

    fun load(uid: String) {
        viewModelScope.launch {
            _state.value = GamesState.Loading
            try {
                _games.value = getGames(uid)
                _state.value = GamesState.Idle
            } catch (e: Exception) {
                _state.value = GamesState.Error(e.message ?: "Error cargando juegos")
            }
        }
    }

    fun add(uid: String, game: Game) {
        viewModelScope.launch {
            _state.value = GamesState.Loading
            try {
                addGame(uid, game)
                _games.value = getGames(uid)
                _state.value = GamesState.Idle
                _events.emit(GamesEvent.SavedOk)
            } catch (e: Exception) {
                _state.value = GamesState.Error(e.message ?: "Error añadiendo juego")
            }
        }
    }

    fun update(uid: String, game: Game) {
        viewModelScope.launch {
            _state.value = GamesState.Loading
            try {
                updateGame(uid, game)
                _games.value = getGames(uid)
                _state.value = GamesState.Idle
                _events.emit(GamesEvent.SavedOk)
            } catch (e: Exception) {
                _state.value = GamesState.Error(e.message ?: "Error actualizando juego")
            }
        }
    }

    fun delete(uid: String, id: String) {
        viewModelScope.launch {
            _state.value = GamesState.Loading
            try {
                deleteGame(uid, id)
                _games.value = getGames(uid)
                _state.value = GamesState.Idle
            } catch (e: Exception) {
                _state.value = GamesState.Error(e.message ?: "Error eliminando juego")
            }
        }
    }

    fun clearError() {
        if (_state.value is GamesState.Error) _state.value = GamesState.Idle
    }
}