package com.match.betweenfriends.data.source

import com.match.betweenfriends.data.database.PlayerDao
import com.match.betweenfriends.data.model.Player
import com.match.betweenfriends.domain.datasource.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val playerDao: PlayerDao,
) : LocalDataSource {

    override suspend fun insertPlayer(player: Player) {
        playerDao.insertPlayer(player)
    }

}