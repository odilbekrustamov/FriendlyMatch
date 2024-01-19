package com.match.betweenfriends.domain.use_case

import com.match.betweenfriends.data.model.Player
import com.match.betweenfriends.domain.repository.MainRepository
import javax.inject.Inject

class InsertPlayerUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {

    suspend fun insertPlayer(player: Player){
        mainRepository.insertPlayer(player)
    }

}