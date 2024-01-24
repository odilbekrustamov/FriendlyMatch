package com.match.betweenfriends.presentation.activity

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.match.betweenfriends.common.SharedPref
import com.match.betweenfriends.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        sharedPref = SharedPref(this)

        if (sharedPref.sound) {
            viewModel.startService()
        }
    }

    fun startBackgroundMusicService() {
        viewModel.startService()
    }

    fun stopBackgroundMusicService() {
        viewModel.stopService()
    }

    fun vibrate() {
        viewModel.vibrate()
    }

    override fun onStart() {
        if (sharedPref.sound) {
            viewModel.resumeService()
        }
        super.onStart()
    }

    override fun onPause() {
        if (sharedPref.sound) {
            viewModel.pauseService()
        }
        super.onPause()
    }
}