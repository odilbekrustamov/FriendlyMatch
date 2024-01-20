package com.match.betweenfriends.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.match.betweenfriends.R
import com.match.betweenfriends.databinding.FragmentCompositionBinding
import dagger.hilt.android.AndroidEntryPoint


class CompositionFragment : Fragment(R.layout.fragment_composition) {
    private lateinit var binding: FragmentCompositionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCompositionBinding.bind(view)

        initViews()
    }

    private fun initViews() {

    }
}