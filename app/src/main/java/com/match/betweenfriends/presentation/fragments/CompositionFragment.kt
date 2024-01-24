package com.match.betweenfriends.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.match.betweenfriends.common.KeyValues.TEAM1_NAME
import com.match.betweenfriends.common.KeyValues.TEAM1_RESULT
import com.match.betweenfriends.common.KeyValues.TEAM2_NAME
import com.match.betweenfriends.common.KeyValues.TEAM2_RESULT
import com.match.betweenfriends.databinding.FragmentCompositionBinding
import com.match.betweenfriends.presentation.activity.MainActivity
import com.match.betweenfriends.presentation.adapter.CompositionAdapter
import com.match.betweenfriends.presentation.viewmodel.CompositionViewModel
import com.match.betweenfriends.presentation.viewmodel.CompositionViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CompositionFragment : Fragment() {

    private var _binding: FragmentCompositionBinding? = null
    private lateinit var activity: MainActivity
    private val binding get() = _binding!!
    private val viewModel: CompositionViewModel by viewModels {
        CompositionViewModelFactory(requireActivity().applicationContext)
    }
    private val adapter1 by lazy { CompositionAdapter() }
    private val adapter2 by lazy { CompositionAdapter() }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompositionBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        observeComposition()
        return view
    }

    private fun observeComposition() {
        viewModel.team1
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { players ->
                adapter1.submitList(players)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.team2
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { players ->
                adapter2.submitList(players)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { teamNamesAndResults ->
                binding.edtTeamName1.setText(teamNamesAndResults.team1Name)
                binding.edtTeamName2.setText(teamNamesAndResults.team2Name)
                binding.edtScoreTeam1.setText(teamNamesAndResults.team1Result)
                binding.edtScoreTeam2.setText(teamNamesAndResults.team2Result)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initViews() {
        binding.rvTeam1Players.adapter = adapter1
        binding.rvTeam2Players.adapter = adapter2
        binding.rvTeam1Players.itemAnimator = null
        binding.rvTeam2Players.itemAnimator = null

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
            activity.vibrate()
        }

        binding.ivTime.setOnClickListener {
            activity.vibrate()
        }

        updatePlayerNames()
        updateTeamNamesAndResult()
        onBackPressed()
    }

    private fun updateTeamNamesAndResult() {
        binding.edtTeamName1.doAfterTextChanged {
            it?.let {
                viewModel.updateTeam1NameAndResult(it, TEAM1_NAME)
            }
        }
        binding.edtTeamName2.doAfterTextChanged {
            it?.let {
                viewModel.updateTeam1NameAndResult(it, TEAM2_NAME)
            }
        }
        binding.edtScoreTeam1.doAfterTextChanged {
            it?.let {
                viewModel.updateTeam1NameAndResult(it, TEAM1_RESULT)
            }
        }
        binding.edtScoreTeam2.doAfterTextChanged {
            it?.let {
                viewModel.updateTeam1NameAndResult(it, TEAM2_RESULT)
            }
        }
    }

    private fun onBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.updateDatabase()
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
            )
    }

    override fun onPause() {
        viewModel.updateDatabase()
        super.onPause()
    }

    private fun updatePlayerNames() {
        adapter1.onPlayerNameEdit = { name, player ->
            viewModel.updateName(name, player)
        }

        adapter2.onPlayerNameEdit = { name, player ->
            viewModel.updateName(name, player)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}