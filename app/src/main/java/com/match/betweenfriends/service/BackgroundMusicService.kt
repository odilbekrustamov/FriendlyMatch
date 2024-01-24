package com.match.betweenfriends.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.match.betweenfriends.R

class MusicService : Service() {

    private var isPlaying = false
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.all_screens)
        mediaPlayer.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_ACTION -> {
                startMusic()
                isPlaying = true
            }

            STOP_ACTION -> {
                stopMusic()
                isPlaying = false
            }

            RESUME_ACTION -> {
                if (!isPlaying) {
                    resumeMusic()
                    isPlaying = true
                }
            }

            PAUSE_ACTION -> {
                if (isPlaying) {
                    pauseMusic()
                    isPlaying = false
                }
            }
        }
        return START_STICKY
    }

    private fun startMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0) // Rewind to avoid starting from halfway next time
        }
    }

    private fun resumeMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val START_ACTION = "START_MUSIC"
        const val STOP_ACTION = "STOP_MUSIC"
        const val RESUME_ACTION = "RESUME_MUSIC"
        const val PAUSE_ACTION = "PAUSE_MUSIC"
    }
}

