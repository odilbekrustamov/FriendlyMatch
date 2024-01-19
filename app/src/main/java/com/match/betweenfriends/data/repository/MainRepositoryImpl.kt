package com.match.betweenfriends.data.repository

import com.match.betweenfriends.data.model.Player
import com.match.betweenfriends.domain.datasource.LocalDataSource
import com.match.betweenfriends.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
): MainRepository {

    override suspend fun insertPlayer(player: Player) {
        localDataSource.insertPlayer(player)
    }

}