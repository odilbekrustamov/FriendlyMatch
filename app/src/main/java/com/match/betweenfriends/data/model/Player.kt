package com.match.betweenfriends.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class Player(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    var teamId: Int,
    var playerName: String = "",
    var goalsScored: Int = 0,
    var caughtGoals: Int = 0,
    var ballsIntercepted: Int = 0,
    var penalties: Int = 0,
    var cornerKicks: Int = 0,
    var redCards: Int = 0,
    var yellowCards: Int = 0,
    var offenses: Int = 0
    )