package com.match.betweenfriends.presentation.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import com.match.betweenfriends.common.SharedPref
import com.match.betweenfriends.service.MusicService


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val sharedPref = SharedPref(context)

    fun startService() {
        Intent(context, MusicService::class.java).also { intent ->
            intent.action = MusicService.START_ACTION
            context.startService(intent)
        }
    }

    fun stopService() {
        Intent(context, MusicService::class.java).also { intent ->
            intent.action = MusicService.STOP_ACTION
            context.stopService(intent)
        }
    }

    fun resumeService() {
        Intent(context, MusicService::class.java).also { intent ->
            intent.action = MusicService.RESUME_ACTION
            context.startService(intent)
        }
    }

    fun pauseService() {
        Intent(context, MusicService::class.java).also { intent ->
            intent.action = MusicService.PAUSE_ACTION
            context.startService(intent)
        }
    }

    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private var vibrationEffect: VibrationEffect? = null

    fun vibrate() {
        if (sharedPref.vibration) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    vibrationEffect =
                        VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE);
                    vibrator.cancel()
                    vibrator.vibrate(vibrationEffect)
                } catch (e: Exception) {
                }
            }
        }
    }
}