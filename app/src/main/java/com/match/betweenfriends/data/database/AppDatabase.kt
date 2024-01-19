package com.match.betweenfriends.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.match.betweenfriends.data.model.Player

@Database(entities = [Player::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun playerDao(): PlayerDao

}