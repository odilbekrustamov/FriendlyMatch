package com.match.betweenfriends.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.match.betweenfriends.common.SharedPref
import com.match.betweenfriends.data.database.AppDatabase
import com.match.betweenfriends.data.model.Player
import com.match.betweenfriends.data.model.ValueType
import com.match.betweenfriends.data.source.LocalDataSourceImpl
import com.match.betweenfriends.domain.datasource.LocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeamUiState(
    val team1Players: List<Player> = emptyList(),
    val team2Players: List<Player> = emptyList(),
    val team1Name: String = "TEAM1",
    val team2Name: String = "TEAM2",
    val result: String = "0:0",
    val chosenIndex: Int? = null
)


class TeamsViewModel(private val localDataSource: LocalDataSource) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamUiState())
    val uiState get() = _uiState.asStateFlow()


    private fun team1Composition() {
        viewModelScope.launch {
            localDataSource.getTeam1Composition()
                .flowOn(Dispatchers.IO)
                .collectLatest { players ->
                    _uiState.update {
                        it.copy(team1Players = players)
                    }
                }
        }
    }

    private fun team2Composition() {
        viewModelScope.launch {
            localDataSource.getTeam2Composition()
                .flowOn(Dispatchers.IO)
                .collectLatest { players ->
                    _uiState.update {
                        it.copy(team2Players = players)
                    }
                }
        }
    }

    private fun getNameAndResults() {
        val teamNames = localDataSource.getTeamNames()
        val results = localDataSource.getResult()
        _uiState.update {
            it.copy(
                team1Name = teamNames.first,
                team2Name = teamNames.second,
                result = results.first.toString().plus(":").plus(results.second.toString()),
            )
        }
    }

    fun onPlayerChosen(index: Int) {
        _uiState.update {
            it.copy(chosenIndex = index)
        }
    }

    fun onValueChange(s: String, player: Player, valueType: ValueType) {
        if (s.isNotBlank()) {
            when (valueType) {
                ValueType.GoalsScored -> {
                    _uiState.value.team1Players.find { it.userId == player.userId }?.goalsScored =
                        s.trim().toInt()
                    _uiState.value.team2Players.find { it.userId == player.userId }?.goalsScored =
                        s.trim().toInt()
                }

                ValueType.CaughtGoals -> {
                    _uiState.value.team1Players.find { it.userId == player.userId }?.caughtGoals =
                        s.trim().toInt()
                    _uiState.value.team2Players.find { it.userId == player.userId }?.caughtGoals =
                        s.trim().toInt()
                }

                ValueType.BallsIntercepted -> {
                    _uiState.value.team1Players.find { it.userId == player.userId }?.ballsIntercepted =
                        s.trim().toInt()
                    _uiState.value.team2Players.find { it.userId == player.userId }?.ballsIntercepted =
                        s.trim().toInt()
                }

                ValueType.Penalties -> {
                    _uiState.value.team1Players.find { it.userId == player.userId }?.penalties =
                        s.trim().toInt()
                    _uiState.value.team2Players.find { it.userId == player.userId }?.penalties =
                        s.trim().toInt()
                }

                ValueType.CornerKicks -> {
                    _uiState.value.team1Players.find { it.userId == player.userId }?.cornerKicks =
                        s.trim().toInt()
                    _uiState.value.team2Players.find { it.userId == player.userId }?.cornerKicks =
                        s.trim().toInt()
                }

                ValueType.RedCards -> {
                    _uiState.value.team1Players.find { it.userId == player.userId }?.redCards =
                        s.trim().toInt()
                    _uiState.value.team2Players.find { it.userId == player.userId }?.redCards =
                        s.trim().toInt()
                }

                ValueType.YellowCards -> {
                    _uiState.value.team1Players.find { it.userId == player.userId }?.yellowCards =
                        s.trim().toInt()
                    _uiState.value.team2Players.find { it.userId == player.userId }?.yellowCards =
                        s.trim().toInt()
                }

                ValueType.Offenses -> {
                    _uiState.value.team1Players.find { it.userId == player.userId }?.offenses =
                        s.trim().toInt()
                    _uiState.value.team2Players.find { it.userId == player.userId }?.offenses =
                        s.trim().toInt()
                }
            }
        }
    }

    fun cancelShowDetails() {
        _uiState.update {
            it.copy(chosenIndex = null)
        }
    }

    fun updateChangesLocally() {
        viewModelScope.launch {
            val players = _uiState.value.team1Players + _uiState.value.team2Players
            localDataSource.insertAll(players)
        }
    }

    init {
        team1Composition()
        team2Composition()
        getNameAndResults()
    }
}

@Suppress("UNCHECKED_CAST")
class TeamsViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        val userDao = db.playerDao()
        val sharedPref = SharedPref(context)
        val localDataSource = LocalDataSourceImpl(userDao, sharedPref)
        return TeamsViewModel(localDataSource) as T
    }
}