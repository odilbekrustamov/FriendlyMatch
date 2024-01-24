package com.match.betweenfriends.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.match.betweenfriends.common.KeyValues.TEAM1_ID
import com.match.betweenfriends.common.KeyValues.TEAM2_ID

@Entity(tableName = "player")
data class Player(
    @PrimaryKey(autoGenerate = true) val userId: Int? = null,
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
) {

    override fun toString(): String {
        return "Name: $playerName\n" +
                "Goals Scored: $goalsScored\n" +
                "Caught Goals: $caughtGoals\n" +
                "Balls Intercepted: $ballsIntercepted\n" +
                "Penalties: $penalties\n" +
                "Corner Kicks: $cornerKicks\n" +
                "Red Cards: $redCards\n" +
                "Yellow  Cards: $yellowCards\n"
    }
}

typealias PLAYERS = List<Player>

val Team1Default = List(11) {
    Player(teamId = TEAM1_ID)
}

val Team2Default = List(11) {
    Player(teamId = TEAM2_ID)
}

enum class ValueType {
    GoalsScored,
    CaughtGoals,
    BallsIntercepted,
    Penalties,
    CornerKicks,
    RedCards,
    YellowCards,
    Offenses
}