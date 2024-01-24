package com.match.betweenfriends.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.match.betweenfriends.R
import com.match.betweenfriends.databinding.FragmentSettingsBinding
import com.match.betweenfriends.presentation.activity.MainActivity
import com.match.betweenfriends.presentation.viewmodel.SettingsViewModel
import com.match.betweenfriends.presentation.viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(requireContext())
    }

    private lateinit var activity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        observe()
        return view
    }


    private fun initViews() {

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
            activity.vibrate()
        }

        binding.ivSound.setOnClickListener {
            viewModel.toggleSound()
            activity.vibrate()
        }

        binding.ivVibration.setOnClickListener {
            viewModel.toggleVibration()
            activity.vibrate()
        }
    }

    private fun observe() {
        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { uiState ->
                if (uiState.isSoundEnabled) {
                    binding.ivSound.setImageResource(R.drawable.switch_on)
                } else {
                    binding.ivSound.setImageResource(R.drawable.switch_off)
                }
                if (uiState.isVibrationEnabled) {
                    binding.ivVibration.setImageResource(R.drawable.switch_on)
                } else {
                    binding.ivVibration.setImageResource(R.drawable.switch_off)
                }

                if (uiState.isSoundEnabled) {
                    activity.startBackgroundMusicService()
                } else {
                    activity.stopBackgroundMusicService()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}