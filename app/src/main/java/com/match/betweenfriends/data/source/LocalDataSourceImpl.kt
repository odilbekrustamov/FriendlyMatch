package com.match.betweenfriends.data.source

import com.match.betweenfriends.common.SharedPref
import com.match.betweenfriends.data.database.PlayerDao
import com.match.betweenfriends.data.model.PLAYERS
import com.match.betweenfriends.data.model.Player
import com.match.betweenfriends.domain.datasource.LocalDataSource
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    private val playerDao: PlayerDao,
    private val sharedPref: SharedPref
) : LocalDataSource {
    override suspend fun insertAll(players: List<Player>) {
        playerDao.insertAll(players)
    }

    override fun getTeam1Composition(): Flow<PLAYERS> {
        return playerDao.getTeam1Composition()
    }

    override fun getTeam2Composition(): Flow<PLAYERS> {
        return playerDao.getTeam2Composition()
    }

    override fun saveTeamNames(team1: String, team2: String) {
        sharedPref.team1 = team1
        sharedPref.team2 = team2
    }

    override fun saveResult(team1: Int, team2: Int) {
        sharedPref.team1Result = team1
        sharedPref.team2Result = team2
    }

    override fun getTeamNames(): Pair<String, String> {
        return Pair(
            sharedPref.team1,
            sharedPref.team2
        )
    }

    override fun getResult(): Pair<Int, Int> {
        return Pair(
            sharedPref.team1Result,
            sharedPref.team2Result
        )
    }
}