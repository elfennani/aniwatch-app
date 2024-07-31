package com.elfennani.aniwatch.presentation.screens.episode

import android.app.ProgressDialog.show
import android.content.pm.ActivityInfo
import android.text.TextUtils.replace
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.AspectRatioFrameLayout.ResizeMode
import androidx.media3.ui.PlayerView
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elfennani.aniwatch.presentation.theme.AppTheme
import com.elfennani.aniwatch.requireActivity

const val TAG = "EpisodeScreen"

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EpisodeScreen(
    navController: NavController,
    state: EpisodeUiState,
    onRefresh: () -> Unit = {},
    onSetResolution: (index: Int) -> Unit = {}
) {
    val context = LocalContext.current


    DisposableEffect(context) {
        context.requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        onDispose {
            context.requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    HideSystemBars()
    Surface(
        color = Color.Black,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = state.exoPlayer
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                    }
                },
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun QualitySelector(
    exoPlayer: ExoPlayer,
    trackGroup: Tracks.Group,
    onSetResolution: (index: Int) -> Unit
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal)) {
        val isNoneSelected = exoPlayer.trackSelectionParameters.overrides.size == 0
        for (i in 0 until trackGroup.length) {
            val isSelected = exoPlayer.trackSelectionParameters.overrides.any { override ->
                override.value.trackIndices.contains(i)
            }
            val trackFormat = trackGroup.getTrackFormat(i)
            Log.d(TAG, "EpisodeScreen: ")

            Button(
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = AppTheme.colorScheme.primary,
                    contentColor = AppTheme.colorScheme.onPrimary
                ),
                shape = AppTheme.shapes.button,
                onClick = { onSetResolution(i) },
                enabled = isNoneSelected || !isSelected
            ) {
                Text(text = trackFormat.height.toString())
            }
        }
    }
}

@Composable
fun HideSystemBars() {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val window = context.requireActivity().window ?: return@DisposableEffect onDispose {}
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            insetsController.apply {
                show(WindowInsetsCompat.Type.statusBars())
                show(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }
}

const val EPISODE_SCREEN_PATTERN = "episode/{id}/{allanimeId}/{episode}"

@androidx.annotation.OptIn(UnstableApi::class)
fun NavGraphBuilder.episodeScreen(navController: NavController) {
    composable(
        route = EPISODE_SCREEN_PATTERN,
        arguments = listOf(
            navArgument("id") { type = NavType.IntType },
            navArgument("allanimeId") { type = NavType.StringType },
            navArgument("episode") { type = NavType.IntType }
        ),
    ) {
        val viewModel: EpisodeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        EpisodeScreen(
            navController = navController,
            state = state,
            onRefresh = { viewModel.fetchEpisode() },
            onSetResolution = viewModel::changeResolution
        )
    }
}

fun NavController.navigateToEpisodeScreen(
    id: Int,
    allanimeId: String,
    episode: Int,
    popUpToTop: Boolean = false
) {
    this.navigate(
        EPISODE_SCREEN_PATTERN
            .replace("{id}", id.toString())
            .replace("{allanimeId}", allanimeId)
            .replace("{episode}", episode.toString())
    ) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}