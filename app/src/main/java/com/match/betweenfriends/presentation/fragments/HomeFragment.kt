package com.match.betweenfriends.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.match.betweenfriends.R
import com.match.betweenfriends.common.SharedPref
import com.match.betweenfriends.data.database.AppDatabase
import com.match.betweenfriends.data.model.Team1Default
import com.match.betweenfriends.data.model.Team2Default
import com.match.betweenfriends.databinding.FragmentHomeBinding
import com.match.betweenfriends.presentation.activity.MainActivity
import com.match.betweenfriends.presentation.theme.FriendlyMatchTheme
import com.match.betweenfriends.presentation.theme.Grad1
import com.match.betweenfriends.presentation.theme.TabActive
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: MainActivity
    private lateinit var sharedPref: SharedPref

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as MainActivity
        sharedPref = SharedPref(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                FriendlyMatchTheme {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Column(
                            modifier = Modifier
                                .padding(top = 150.dp, start = 32.dp, end = 32.dp)
                                .align(Alignment.TopStart)
                                .fillMaxWidth()
                                .wrapContentWidth(),
                            verticalArrangement = Arrangement.spacedBy(32.dp)
                        ) {
                            HomeButton(id = R.string.str_settings) {
                                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                                activity.vibrate()
                            }

                            HomeButton(id = R.string.str_composition) {
                                findNavController().navigate(R.id.action_homeFragment_to_compositionFragment)
                                activity.vibrate()
                            }

                            HomeButton(id = R.string.str_teams) {
                                findNavController().navigate(R.id.action_homeFragment_to_teamsFragment)
                                activity.vibrate()
                            }
                        }
                    }
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkDb()
    }

    private fun checkDb() {
        if (sharedPref.firstTime) {
            val playerDao = AppDatabase.getInstance(requireContext().applicationContext).playerDao()
            viewLifecycleOwner.lifecycleScope.launch {
                playerDao.insertAll(Team1Default)
                playerDao.insertAll(Team2Default)
            }
            sharedPref.firstTime = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@Composable
fun HomeButton(
    @StringRes id: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .advancedShadow(
                color = Color.Black.copy(0.35f),
                alpha = 0.35f,
                shadowBlurRadius = 8.dp,
                offsetY = 7.dp,
                offsetX = 6.dp,
                cornersRadius = 80.dp
            )
            .clip(MaterialTheme.shapes.large)
            .background(
                Brush.verticalGradient(
                    listOf(TabActive, Grad1)
                )
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = id),
            style = MaterialTheme.typography.labelMedium
        )
    }
}