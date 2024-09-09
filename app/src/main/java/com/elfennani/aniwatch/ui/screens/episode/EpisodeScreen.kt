package com.elfennani.aniwatch.ui.screens.episode

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.ui.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.ui.composables.KeepScreenON
import com.elfennani.aniwatch.ui.theme.AppTheme
import com.elfennani.aniwatch.utils.requireActivity
import kotlinx.serialization.Serializable

const val TAG = "EpisodeScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun EpisodeScreen(
    navController: NavController,
    state: EpisodeUiState,
    onRefresh: () -> Unit = {},
    onSetResolution: (index: Int) -> Unit = {},
    onErrorDismiss: (errorId:Int) -> Unit
) {
    val context = LocalContext.current


    DisposableEffect(context) {
        context.requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        onDispose {
            context.requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    KeepScreenON()
    HideSystemBars()

    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.White,
        snackbarHost = { ErrorSnackbarHost(errors = state.errors, onErrorDismiss)}
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

@Serializable
data class EpisodeRoute(
    val id: Int,
    val allanimeId: String,
    val episode: Int,
    val audio: EpisodeAudio
)

@androidx.annotation.OptIn(UnstableApi::class)
fun NavGraphBuilder.episodeScreen(navController: NavController) {
    composable<EpisodeRoute>{
        val viewModel: EpisodeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        EpisodeScreen(
            navController = navController,
            state = state,
            onRefresh = { viewModel.fetchEpisode() },
            onSetResolution = viewModel::changeResolution,
            onErrorDismiss = viewModel::dismissError
        )
    }
}