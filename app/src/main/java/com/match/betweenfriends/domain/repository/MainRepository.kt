package com.match.betweenfriends.domain.repository

import com.match.betweenfriends.data.model.Player

interface MainRepository {

    suspend fun insertPlayer(player: Player)

}