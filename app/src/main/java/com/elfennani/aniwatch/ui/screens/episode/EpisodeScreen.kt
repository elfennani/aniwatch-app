package com.elfennani.aniwatch.ui.screens.episode

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.elfennani.aniwatch.ui.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.ui.composables.KeepScreenON
import com.elfennani.aniwatch.utils.requireActivity

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun EpisodeScreen(
    state: EpisodeUiState,
    onRefresh: () -> Unit = {},
    onErrorDismiss: (errorId: Int) -> Unit,
) {


    KeepLandscape()
    KeepScreenON()
    HideSystemBars()

    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.White,
        snackbarHost = {
            ErrorSnackbarHost(
                errors = state.errors,
                onErrorDismiss = onErrorDismiss,
                actionName = "Retry",
                onPressAction = onRefresh
            )
        }
    ) {
        if(state.isLoading){
            Column(
                modifier = Modifier.padding(it).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        if (state.exoPlayer != null) {
            Box {
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
}

@Composable
private fun KeepLandscape() {
    val context = LocalContext.current


    DisposableEffect(context) {
        context.requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        onDispose {
            context.requireActivity().requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
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

