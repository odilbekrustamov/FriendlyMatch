package com.match.betweenfriends.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.match.betweenfriends.R
import com.match.betweenfriends.common.KeyValues.FIRSTTIME
import com.match.betweenfriends.common.SharedPref
import com.match.betweenfriends.data.model.Player
import com.match.betweenfriends.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPref: SharedPref
    private var players: ArrayList<Player> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        sharedPref = SharedPref(requireContext())

        initViews()
    }

    private fun initViews() {
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        binding.btnCompositions.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_compositionFragment)
        }
        binding.btnTeams.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_teamsFragment)
        }

        if (sharedPref.getIsFirstTime(FIRSTTIME)){
            sharedPref.saveIsFirstTime(FIRSTTIME, false)
            for (i in 1..11){
                val id = if (i > 0){  "0${i}" }else{ "$i"} + ". Player Name"
                val player = Player(userId = 1,teamId = 1001, caughtGoals = 100)
                players.add(Player(userId = 1,teamId = 1001, playerName =  "id"))
            }
        }
    }
}