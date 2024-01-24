package com.match.betweenfriends.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.match.betweenfriends.data.model.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Player>)

    @Query("SELECT * FROM player WHERE teamId= 1")
    fun getTeam1Composition(): Flow<List<Player>>

    @Query("SELECT * FROM player WHERE teamId= 2")
    fun getTeam2Composition(): Flow<List<Player>>

}