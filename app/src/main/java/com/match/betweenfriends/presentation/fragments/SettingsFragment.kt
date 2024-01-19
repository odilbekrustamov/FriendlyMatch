package com.match.betweenfriends.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.match.betweenfriends.R
import com.match.betweenfriends.common.SharedPref
import com.match.betweenfriends.databinding.FragmentSettingsBinding
import com.match.betweenfriends.presentation.activity.MainActivity
import com.match.betweenfriends.common.KeyValues.SOUND
import com.match.betweenfriends.common.KeyValues.VIBRATION

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPref: SharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)
        sharedPref = SharedPref(requireContext())

        initViews()
    }

    private fun initViews() {

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        if (sharedPref.getIsSound(SOUND)){
            binding.ivSound.setImageResource(R.drawable.switch_on)
        }else{
            binding.ivSound.setImageResource(R.drawable.switch_off)
        }

        if (sharedPref.getIsVibration(VIBRATION)){
            binding.ivVibration.setImageResource(R.drawable.switch_on)
        }else{
            binding.ivVibration.setImageResource(R.drawable.switch_off)
        }

        binding.ivSound.setOnClickListener {
            if (sharedPref.getIsSound(SOUND)){
                sharedPref.saveIsSound(SOUND, false)
                (activity as MainActivity?)!!.mMediaPlayer.stop()
                binding.ivSound.setImageResource(R.drawable.switch_off)
            }else{
                sharedPref.saveIsSound(SOUND, true)
                (activity as MainActivity?)!!.mMediaPlayer.start()
                binding.ivSound.setImageResource(R.drawable.switch_on)
            }
        }

        binding.ivVibration.setOnClickListener {
            if (sharedPref.getIsVibration(VIBRATION)){
                sharedPref.saveIsVibration(VIBRATION, false)
                binding.ivVibration.setImageResource(R.drawable.switch_off)
            }else{
                sharedPref.saveIsVibration(VIBRATION, true)
                binding.ivVibration.setImageResource(R.drawable.switch_on)
            }
        }

    }
}