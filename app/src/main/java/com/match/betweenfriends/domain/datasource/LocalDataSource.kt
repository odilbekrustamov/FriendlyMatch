package com.match.betweenfriends.domain.datasource

import com.match.betweenfriends.data.model.Player

interface LocalDataSource {

    suspend fun insertPlayer(player: Player)

}