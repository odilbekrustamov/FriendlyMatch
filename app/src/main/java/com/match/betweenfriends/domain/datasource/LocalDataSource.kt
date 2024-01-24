package com.match.betweenfriends.domain.datasource

import com.match.betweenfriends.data.model.PLAYERS
import com.match.betweenfriends.data.model.Player
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun insertAll(players: List<Player>)

    fun getTeam1Composition(): Flow<PLAYERS>

    fun getTeam2Composition(): Flow<PLAYERS>

    fun saveTeamNames(team1: String, team2: String)

    fun saveResult(team1: Int, team2: Int)

    fun getTeamNames(): Pair<String, String>

    fun getResult(): Pair<Int, Int>

}