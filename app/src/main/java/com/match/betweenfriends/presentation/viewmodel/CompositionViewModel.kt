package com.match.betweenfriends.presentation.viewmodel

import android.content.Context
import android.os.CountDownTimer
import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.match.betweenfriends.common.KeyValues.TEAM1_NAME
import com.match.betweenfriends.common.KeyValues.TEAM1_RESULT
import com.match.betweenfriends.common.KeyValues.TEAM2_NAME
import com.match.betweenfriends.common.KeyValues.TEAM2_RESULT
import com.match.betweenfriends.common.SharedPref
import com.match.betweenfriends.data.database.AppDatabase
import com.match.betweenfriends.data.model.Player
import com.match.betweenfriends.data.source.LocalDataSourceImpl
import com.match.betweenfriends.domain.datasource.LocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class UiState(
    val team1Name: String = "TEAM NAME",
    val team2Name: String = "TEAM NAME",
    val team1Result: String = "0",
    val team2Result: String = "0",
)

const val GAME_DURATION_MILLIS = 5_400_000L


fun Long.convertToTimeString(): String {
    return if (this == 0L) "00:00"
    else {
        val minutes = (this / 60000).toInt() // 1 minute = 60 seconds * 1000 milliseconds
        val seconds = ((this % 60000) / 1000).toInt() // 1 second = 1000 milliseconds
        with(StringBuilder()) {
            append(minutes)
            append(":")
            append(if (seconds < 10) "0" else "") // pad seconds with leading zero if necessary
            append(seconds)
            toString()
        }
    }
}

data class AllInCaching(
    val players: MutableList<Player> = mutableListOf(),
    val uiState: UiState = UiState()
)

class CompositionViewModel(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _team1 = MutableStateFlow(list)
    val team1 get() = _team1.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    private val _team2 = MutableStateFlow(list)
    val team2 get() = _team2.asStateFlow()

    private val _time = MutableStateFlow("90:00")
    val time get() = _time.asStateFlow()

    private var _all: AllInCaching = AllInCaching()


    private fun team1Composition() {
        viewModelScope.launch {
            localDataSource.getTeam1Composition()
                .flowOn(Dispatchers.IO)
                .collectLatest { players ->
                    _team1.update {
                        players
                    }
                    _all.players.addAll(players)
                }
        }
    }

    private fun team2Composition() {
        viewModelScope.launch {
            localDataSource.getTeam2Composition()
                .flowOn(Dispatchers.IO)
                .collectLatest { players ->
                    _team2.update {
                        players
                    }
                    _all.players.addAll(players)
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
                team1Result = results.first.toString(),
                team2Result = results.second.toString()
            )
        }
    }

    fun updateName(name: String, player: Player) {
        println(_all)
        _all.players.find { it.userId == player.userId }?.playerName = name
        println(_all)
    }

    fun updateDatabase() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                localDataSource.insertAll(_all.players)
                localDataSource.saveTeamNames(_all.uiState.team1Name, _all.uiState.team2Name)
                localDataSource.saveResult(
                    _all.uiState.team1Result.toInt(),
                    _all.uiState.team2Result.toInt()
                )
            }
        }
    }

    fun updateTeam1NameAndResult(it: Editable, key: String) {
        when (key) {
            TEAM1_NAME -> _all = _all.copy(uiState = _all.uiState.copy(team1Name = it.toString()))
            TEAM2_NAME -> _all = _all.copy(uiState = _all.uiState.copy(team2Name = it.toString()))
            TEAM1_RESULT -> _all =
                _all.copy(uiState = _all.uiState.copy(team1Result = it.toString()))

            TEAM2_RESULT -> _all =
                _all.copy(uiState = _all.uiState.copy(team2Result = it.toString()))
        }
    }

    private fun startTimer() {
        countdownTimer.start()
    }

    fun stopTimer() {
        countdownTimer.cancel()
    }

    private val countdownTimer = object : CountDownTimer(GAME_DURATION_MILLIS, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            _time.update {
                millisUntilFinished.convertToTimeString()
            }
        }

        override fun onFinish() {
            // Handle game over logic here
            _time.update { "00:00" }
            stopTimer()
        }
    }

    init {
        team1Composition()
        team2Composition()
        getNameAndResults()
    }
}

@Suppress("UNCHECKED_CAST")
class CompositionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        val userDao = db.playerDao()
        val sharedPref = SharedPref(context)
        val localDataSource = LocalDataSourceImpl(userDao, sharedPref)
        return CompositionViewModel(localDataSource) as T
    }
}

val list = List(11) {
    Player(teamId = 0, playerName = "Player Name")
}