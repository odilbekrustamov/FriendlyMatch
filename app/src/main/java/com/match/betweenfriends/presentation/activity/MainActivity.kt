package com.match.betweenfriends.presentation.activity

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.match.betweenfriends.R
import com.match.betweenfriends.common.SharedPref
import com.match.betweenfriends.databinding.ActivityMainBinding
import com.match.betweenfriends.common.KeyValues.SOUND

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPref: SharedPref
    lateinit var mMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        sharedPref = SharedPref(this)

        mMediaPlayer = MediaPlayer.create(this, R.raw.all_screens)


        if (sharedPref.getIsSound(SOUND)) {
            val audioManager =
                getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            mMediaPlayer.isLooping = true
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer.start()
        }
    }

    override fun onStop() {
        super.onStop()
        mMediaPlayer.stop()
    }
}