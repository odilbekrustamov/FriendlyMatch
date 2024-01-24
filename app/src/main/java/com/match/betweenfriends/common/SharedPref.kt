package com.match.betweenfriends.common

import android.content.Context
import com.match.betweenfriends.common.KeyValues.FIRST_TIME
import com.match.betweenfriends.common.KeyValues.SHARED_PREF
import com.match.betweenfriends.common.KeyValues.SOUND
import com.match.betweenfriends.common.KeyValues.TEAM1_NAME
import com.match.betweenfriends.common.KeyValues.TEAM1_RESULT
import com.match.betweenfriends.common.KeyValues.TEAM2_NAME
import com.match.betweenfriends.common.KeyValues.TEAM2_RESULT
import com.match.betweenfriends.common.KeyValues.VIBRATION

class SharedPref(context: Context) {

    private val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    var sound: Boolean
        set(value) {
            sharedPref.edit().putBoolean(SOUND, value).apply()
        }
        get() = sharedPref.getBoolean(SOUND, false)

    var vibration: Boolean
        set(value) {
            sharedPref.edit().putBoolean(VIBRATION, value).apply()
        }
        get() = sharedPref.getBoolean(VIBRATION, false)

    var firstTime: Boolean
        set(value) {
            sharedPref.edit().putBoolean(FIRST_TIME, value).apply()
        }
        get() = sharedPref.getBoolean(FIRST_TIME, true)


    //team
    var team1: String
        set(value) {
            sharedPref.edit().putString(TEAM1_NAME, value).apply()
        }
        get() = sharedPref.getString(TEAM1_NAME, "TEAM NAME") ?: "TEAM NAME"

    var team2: String
        set(value) {
            sharedPref.edit().putString(TEAM2_NAME, value).apply()
        }
        get() = sharedPref.getString(TEAM2_NAME, "TEAM NAME") ?: "TEAM NAME"

    //result

    var team1Result: Int
        set(value) {
            sharedPref.edit().putInt(TEAM1_RESULT, value).apply()
        }
        get() = sharedPref.getInt(TEAM1_RESULT, 0)

    var team2Result: Int
        set(value) {
            sharedPref.edit().putInt(TEAM2_RESULT, value).apply()
        }
        get() = sharedPref.getInt(TEAM2_RESULT, 0)


}