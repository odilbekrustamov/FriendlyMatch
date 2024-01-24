package com.match.betweenfriends.presentation.fragments

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.match.betweenfriends.R
import com.match.betweenfriends.common.KeyValues.DATA_TYPE
import com.match.betweenfriends.data.model.Player
import com.match.betweenfriends.data.model.ValueType
import com.match.betweenfriends.databinding.FragmentTeamsBinding
import com.match.betweenfriends.presentation.activity.MainActivity
import com.match.betweenfriends.presentation.theme.BLUE
import com.match.betweenfriends.presentation.theme.FriendlyMatchTheme
import com.match.betweenfriends.presentation.theme.Grad1
import com.match.betweenfriends.presentation.theme.Grad2
import com.match.betweenfriends.presentation.theme.Grad3
import com.match.betweenfriends.presentation.theme.Gradient1
import com.match.betweenfriends.presentation.theme.Gradient2
import com.match.betweenfriends.presentation.theme.TabActive
import com.match.betweenfriends.presentation.theme.TabInactive
import com.match.betweenfriends.presentation.viewmodel.TeamUiState
import com.match.betweenfriends.presentation.viewmodel.TeamsViewModel
import com.match.betweenfriends.presentation.viewmodel.TeamsViewModelFactory


class TeamsFragment : Fragment() {

    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TeamsViewModel by viewModels {
        TeamsViewModelFactory(requireContext().applicationContext)
    }

    private lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState by viewModel.uiState.collectAsState()
                activity = LocalContext.current.getActivity() as MainActivity
                // In Compose world
                FriendlyMatchTheme {
                    TeamContent(
                        uiState,
                        onBackPressed = {
                            if (uiState.chosenIndex != null) {
                                viewModel.cancelShowDetails()
                            } else {
                                findNavController().navigateUp()
                            }
                            activity.vibrate()
                        },
                        onSharePressed = { player ->
                            sharePlayerTotals(player)
                            activity.vibrate()
                        },
                        onValueChanged = { value, player, type ->
                            viewModel.onValueChange(value, player, type)
                            activity.vibrate()
                        },
                        onPlayerChosen = { index ->
                            viewModel.onPlayerChosen(index = index)
                            activity.vibrate()
                        },
                        onTabChange = {
                            activity.vibrate()
                        }
                    )
                }
            }
        }
        onBackPressed()
        return view
    }

    private fun sharePlayerTotals(player: Player) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = DATA_TYPE
            putExtra(Intent.EXTRA_TEXT, player.toString())
        }
        startActivity(Intent.createChooser(intent, "Share ${player.playerName}'s totals"))
    }

    private fun onBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.uiState.value.chosenIndex != null) {
                        viewModel.cancelShowDetails()
                    } else {
                        viewModel.updateChangesLocally()
                        if (isEnabled) {
                            isEnabled = false
                            requireActivity().onBackPressed()
                        }
                    }
                }
            }
            )
    }

    override fun onPause() {
        viewModel.updateChangesLocally()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@Composable
fun TeamContent(
    uiState: TeamUiState,
    onBackPressed: () -> Unit,
    onValueChanged: (String, Player, ValueType) -> Unit,
    onPlayerChosen: (index: Int) -> Unit,
    onSharePressed: (Player) -> Unit,
    onTabChange: () -> Unit
) {

    var tabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = tabIndex, indicator = {}) {

                val onSelect: (Int) -> Unit = {
                    if (uiState.chosenIndex == null) {
                        tabIndex = it
                        onTabChange()
                    }
                }

                Box(
                    modifier = Modifier
                        .advancedShadow(
                            color = Color.Black.copy(0.25f),
                            alpha = 0.25f,
                            shadowBlurRadius = 3.dp,
                            offsetY = 3.dp
                        )
                        .background(if (tabIndex == 0) TabActive else TabInactive)
                        .clickable {
                            onSelect(0)
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.team1Name,
                        style = MaterialTheme.typography.titleLarge
                    )
                }


                Box(
                    modifier = Modifier
                        .advancedShadow(
                            color = Color.Black.copy(0.25f),
                            alpha = 0.25f,
                            shadowBlurRadius = 3.dp,
                            offsetY = 3.dp
                        )
                        .background(if (tabIndex == 1) TabActive else TabInactive)
                        .clickable {
                            onSelect(1)
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.team2Name,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        content = { paddingValues ->


            Column(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .background(Brush.horizontalGradient(listOf(Gradient1, Gradient2)))
                    .padding(top = 30.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResultContent(
                    result = uiState.result,
                    player = if (uiState.chosenIndex == null) null else if (tabIndex == 0) uiState.team1Players[uiState.chosenIndex] else uiState.team2Players[uiState.chosenIndex],
                    index = uiState.chosenIndex
                )

                if (uiState.chosenIndex == null) {
                    PlayerNames(
                        if (tabIndex == 0) uiState.team1Players else uiState.team2Players,
                        onPlayerChosen = onPlayerChosen
                    )
                } else {
                    PlayerDetails(
                        player = if (tabIndex == 0) uiState.team1Players[uiState.chosenIndex] else uiState.team2Players[uiState.chosenIndex],
                        onValueChanged
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackPressed, modifier = Modifier.size(64.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (uiState.chosenIndex != null) {
                        IconButton(
                            onClick = {
                                onSharePressed(if (tabIndex == 0) uiState.team1Players[uiState.chosenIndex] else uiState.team2Players[uiState.chosenIndex])
                            },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun PlayerDetails(
    player: Player,
    onValueChanged: (String, Player, ValueType) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .advancedShadow(
                color = Color.Black.copy(0.25f),
                alpha = 0.25f,
                shadowBlurRadius = 4.dp,
                offsetY = 4.dp
            )
            .clip(MaterialTheme.shapes.small)
            .background(
                Brush.horizontalGradient(
                    listOf(Grad1, Grad2, Grad3)
                )
            )
            .padding(top = 8.dp, end = 8.dp, start = 8.dp, bottom = 16.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DetailRow(valueNameResId = R.string.goals_scored, value = player.goalsScored) {
                onValueChanged(it, player, ValueType.GoalsScored)
            }
            DetailRow(valueNameResId = R.string.caught_goals, value = player.caughtGoals) {
                onValueChanged(it, player, ValueType.CaughtGoals)
            }
            DetailRow(
                valueNameResId = R.string.balls_intercepted,
                value = player.ballsIntercepted
            ) {
                onValueChanged(it, player, ValueType.BallsIntercepted)
            }
            DetailRow(valueNameResId = R.string.penalties, value = player.penalties) {
                onValueChanged(it, player, ValueType.Penalties)
            }
            DetailRow(valueNameResId = R.string.corner_kicks, value = player.cornerKicks) {
                onValueChanged(it, player, ValueType.CornerKicks)
            }
            DetailRow(valueNameResId = R.string.red_cards, value = player.redCards) {
                onValueChanged(it, player, ValueType.RedCards)
            }
            DetailRow(valueNameResId = R.string.yellow_cards, value = player.yellowCards) {
                onValueChanged(it, player, ValueType.YellowCards)
            }
            DetailRow(valueNameResId = R.string.offenses, value = player.offenses) {
                onValueChanged(it, player, ValueType.Offenses)
            }
        }
    }
}

@Composable
fun DetailRow(
    modifier: Modifier = Modifier,
    @StringRes valueNameResId: Int,
    value: Int,
    onValueChanged: (String) -> Unit
) {

    Row(modifier) {
        Text(
            text = stringResource(id = valueNameResId),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .weight(4f)
                .advancedShadow(
                    color = Color.Black.copy(0.25f),
                    alpha = 0.25f,
                    shadowBlurRadius = 4.dp,
                    offsetY = 4.dp
                )
                .background(BLUE)
                .padding(top = 2.dp, bottom = 2.dp, start = 32.dp, end = 12.dp)
        )
        Spacer(modifier = Modifier.weight(.25f))

        var text by remember {
            mutableStateOf(value.toString())
        }
        BasicTextField(
            value = text, // Use the initial value
            onValueChange = {
                text = it
                onValueChanged(text)
            },
            textStyle = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .weight(1.5f)
                .advancedShadow(
                    color = Color.Black.copy(0.25f),
                    alpha = 0.25f,
                    shadowBlurRadius = 4.dp,
                    offsetY = 4.dp
                )
                .background(BLUE)
                .padding(vertical = 2.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
    }
}

@Composable
fun ResultContent(modifier: Modifier = Modifier, result: String, player: Player?, index: Int?) {
    if (index == null) {
        Box(
            modifier = modifier.size(185.dp, 38.dp),
            contentAlignment = Alignment.Center,
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_score), contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Text(
                text = result,
                style = MaterialTheme.typography.titleMedium
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .fillMaxWidth()
                .advancedShadow(
                    color = Color.Black.copy(0.25f),
                    alpha = 0.25f,
                    shadowBlurRadius = 4.dp,
                    offsetY = 4.dp
                )
                .background(TabInactive)
                .padding(vertical = 8.dp, horizontal = 32.dp)
        ) {
            Text(
                text = if (index < 9) "0${index + 1}. " else "${index + 1}. ",
                style = MaterialTheme.typography.titleSmall.copy(textAlign = TextAlign.End),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = player!!.playerName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(4f)
            )
        }
    }
}

@Composable
fun PlayerNames(players: List<Player>, onPlayerChosen: (Int) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        players.forEachIndexed { index, player ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 50.dp)
                    .fillMaxWidth()
                    .advancedShadow(
                        color = Color.Black.copy(0.25f),
                        alpha = 0.25f,
                        shadowBlurRadius = 4.dp,
                        offsetY = 4.dp
                    )
                    .background(TabInactive)
                    .clickable { onPlayerChosen(index) }
                    .padding(vertical = 4.dp, horizontal = 32.dp)
            ) {
                Text(
                    text = if (index < 9) "0${index + 1}. " else "${index + 1}. ",
                    style = MaterialTheme.typography.titleSmall.copy(textAlign = TextAlign.End),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = player.playerName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(4f)
                )
            }
        }
    }
}

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = androidx.compose.ui.graphics.Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> throw IllegalArgumentException()
}