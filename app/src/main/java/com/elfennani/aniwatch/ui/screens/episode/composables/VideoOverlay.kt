package com.elfennani.aniwatch.ui.screens.episode.composables

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.FastRewind
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.elfennani.aniwatch.ui.theme.AppTheme
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
private fun Long?.toReadable(forceHours: Boolean = false): String {
    if (this == null || this < 0) {
        return if (forceHours) "--:--:--" else "--:--"
    }

    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (forceHours)
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    else
        String.format("%02d:%02d", minutes, seconds)
}

@Composable
fun VideoOverlay(
    modifier: Modifier = Modifier,
    videoState: VideoState,
    onBack: () -> Unit = {},
) {
    var sliderValue by remember { mutableStateOf(0f) }
    var isSeeking by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(true) }
    var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }
    val backdropOpacity by animateFloatAsState(
        targetValue = if (visible) 0.4f else 0f,
    )

    LaunchedEffect(videoState.currentPosition, videoState.duration) {
        if (videoState.currentPosition != null && videoState.duration != null && !isSeeking) {
            sliderValue = videoState.currentPosition!!.toFloat() / videoState.duration!!.toFloat()
        }
    }

    LaunchedEffect(lastInteractionTime) {
        kotlinx.coroutines.delay(3000)
        if (System.currentTimeMillis() - lastInteractionTime >= 3000 && visible && videoState.isPlaying) {
            visible = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(backdropOpacity))
            .clickable(interactionSource = null, indication = null) {
                lastInteractionTime = System.currentTimeMillis()
                visible = !visible
            }
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White.copy(0.9f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedVisibility(
                    visible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onBack
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back")
                        }
                        TextButton(onClick = {
                            if (videoState.currentPosition != null)
                                videoState.seekTo(videoState.currentPosition!! + 1000L * 85)
                        }) {
                            Text("Skip Intro", color = Color.White)
                        }
                    }
                }
                AnimatedVisibility(
                    visible,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = videoState.currentPosition
                                .toReadable(
                                    forceHours = videoState.duration != null &&
                                            videoState.duration!! > 1000L * 60 * 60
                                ),
                            style = AppTheme.typography.labelSmallBold,
                            modifier = Modifier.width(96.dp),
                            textAlign = TextAlign.Center
                        )
                        Slider(
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = AppTheme.colorScheme.primary,
                                activeTrackColor = AppTheme.colorScheme.primary,
                                inactiveTrackColor = Color.White.copy(0.33f)
                            ),
                            value = sliderValue,
                            onValueChange = {
                                lastInteractionTime = System.currentTimeMillis()
                                sliderValue = it
                                if (!isSeeking)
                                    isSeeking = true
                            },
                            onValueChangeFinished = {
                                videoState.duration?.let { duration ->
                                    videoState.seekTo((duration * sliderValue).toLong())
                                }
                                isSeeking = false;
                            },
                        )
                        Text(
                            videoState.duration.toReadable(),
                            style = AppTheme.typography.labelSmallBold,
                            modifier = Modifier.width(96.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }


            AnimatedVisibility(
                visible,
                modifier = Modifier
                    .align(Alignment.Center),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        lastInteractionTime = System.currentTimeMillis()
                        videoState.seekBackwards()
                    }) {
                        Icon(
                            modifier = Modifier.alpha(0.75f),
                            imageVector = Icons.Rounded.FastRewind,
                            contentDescription = "Rewind"
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .clickable {
                                lastInteractionTime = System.currentTimeMillis()
                                videoState.togglePlayback()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (videoState.isBuffering && !videoState.isPlaying)
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = Color.White,
                                strokeCap = StrokeCap.Round,
                                strokeWidth = 4.dp
                            )
                        else
                            Icon(
                                modifier = Modifier.size(48.dp),
                                imageVector = if (videoState.isPlaying)
                                    Icons.Rounded.Pause
                                else
                                    Icons.Rounded.PlayArrow,
                                contentDescription = "Play/Pause"
                            )
                    }
                    IconButton(onClick = {
                        lastInteractionTime = System.currentTimeMillis()
                        videoState.seekForward()
                    }) {
                        Icon(
                            modifier = Modifier.alpha(0.75f),
                            imageVector = Icons.Rounded.FastForward,
                            contentDescription = "Forward"
                        )
                    }
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Preview()
@Composable
private fun VideoOverlayPreview() {
    val context = LocalContext.current;
    val exoPlayer = remember {
        ExoPlayer
            .Builder(context)
            .build()
            .apply {
                playWhenReady = false
                addMediaItem(MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
                prepare()
            }
    }
    val state = rememberVideoState(exoPlayer)

    AppTheme {
        Box() {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        useController = false
                    }
                },
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize()
            )

            VideoOverlay(
                videoState = state
            )
        }
    }
}