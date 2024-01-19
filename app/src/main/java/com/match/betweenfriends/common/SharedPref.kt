package com.match.betweenfriends.common

import android.content.Context

class SharedPref(context: Context) {
    private val sharedPref = context.getSharedPreferences("betweenfriends", Context.MODE_PRIVATE)

    fun saveIsSound(key: String, data: Boolean) {
        sharedPref.edit().putBoolean(key, data).apply()
    }

    fun getIsSound(key: String): Boolean {
        return sharedPref.getBoolean(key, true)
    }

    fun saveIsVibration(key: String, data: Boolean) {
        sharedPref.edit().putBoolean(key, data).apply()
    }

    fun getIsVibration(key: String): Boolean {
        return sharedPref.getBoolean(key, true)
    }

    fun saveIsFirstTime(key: String, data: Boolean) {
        sharedPref.edit().putBoolean(key, data).apply()
    }

    fun getIsFirstTime(key: String): Boolean {
        return sharedPref.getBoolean(key, true)
    }
}