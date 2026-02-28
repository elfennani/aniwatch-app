package com.elfennani.aniwatch.ui.screens.episode.composables

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.FastRewind
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.elfennani.aniwatch.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
fun rememberAudioVolume(shouldListen: Boolean = true): MutableFloatState {
    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    val audio = remember {
        mutableFloatStateOf(
            audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC)
                .let { volume -> (volume.toFloat() / maxVolume) * 100 }
        )
    }
    var audioState by audio

    LaunchedEffect(audioState) {
        val newValue = (maxVolume * (audioState / 100)).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newValue, 0)
    }

    DisposableEffect(shouldListen) {
        if (!shouldListen) return@DisposableEffect onDispose { }
        val filter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                Log.d("AudioVolume", "shouldListen: $shouldListen")
                if (intent?.action == "android.media.VOLUME_CHANGED_ACTION" && shouldListen) {
                    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    audioState = (currentVolume.toFloat() / maxVolume.toFloat()) * 100
                }
            }
        }
        context.registerReceiver(receiver, filter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }


    return audio;
}

@Composable
fun rememberBrightness(): MutableFloatState {
    val view = LocalView.current;
    val activity = view.context as? Activity

    val brightnessState = remember { mutableFloatStateOf(0f) };
    var brightness by brightnessState

    LaunchedEffect(brightness) {
        if (activity != null) {
            val layoutParams: WindowManager.LayoutParams = activity.window.attributes
            layoutParams.screenBrightness =
                if (brightness == 0f) WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE else brightness / 100
            activity.window.attributes = layoutParams
        }
    }

    return brightnessState
}


@Composable
fun rememberTemporaryVisibility(key: Any): Boolean {
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    DisposableEffect(key) {
        isVisible = true
        val job = scope.launch {
            delay(3000)
            isVisible = false
        }
        onDispose {
            job.cancel()
        }
    }

    return isVisible
}

@Composable
fun VideoOverlay(
    modifier: Modifier = Modifier,
    videoState: VideoState,
    onBack: () -> Unit = {},
) {
    val density = LocalDensity.current
    var sliderValue by remember { mutableStateOf(0f) }
    var isSeeking by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(true) }
    var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }
    val backdropOpacity by animateFloatAsState(
        targetValue = if (visible) 0.4f else 0f,
    )

    var brightness by rememberBrightness()
    val brightnessVisible = rememberTemporaryVisibility(brightness)

    var shouldListen by remember { mutableStateOf(true) }
    var audio by rememberAudioVolume(shouldListen)
    val audioVisible = rememberTemporaryVisibility(audio)

    var locked by remember { mutableStateOf(false) }

    LaunchedEffect(videoState.currentPosition, videoState.duration) {
        if (videoState.currentPosition != null && videoState.duration != null && !isSeeking) {
            sliderValue = videoState.currentPosition!!.toFloat() / videoState.duration!!.toFloat()
        }
    }

    LaunchedEffect(lastInteractionTime) {
        delay(3000)
        if (System.currentTimeMillis() - lastInteractionTime >= 3000 && visible && videoState.isPlaying) {
            visible = false
        }
    }

    if (locked)
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(backdropOpacity))
                .clickable(interactionSource = null, indication = null) {
                    lastInteractionTime = System.currentTimeMillis()
                    visible = !visible
                }
                .padding(24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            AnimatedVisibility(
                visible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                TextButton(
                    onClick = {
                        lastInteractionTime = System.currentTimeMillis()
                        locked = false
                    }
                ) {
                    Icon(Icons.Default.LockOpen, "Unlock", tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Unlock", color = Color.White)
                }
            }
        }
    else
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
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconButton(
                                onClick = onBack
                            ) {
                                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                onClick = {
                                    lastInteractionTime = System.currentTimeMillis()
                                    locked = true
                                }
                            ) {
                                Icon(Icons.Default.Lock, "Lock", tint = Color.White)
                                Spacer(Modifier.width(8.dp))
                                Text("Lock", color = Color.White)
                            }
                            TextButton(onClick = {
                                if (videoState.currentPosition != null)
                                    videoState.seekTo(videoState.currentPosition!! + 1000L * 85)
                            }) {
                                Text("Skip Intro", color = Color.White)
                            }
                        }
                    }
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        Row(
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .pointerInput(Unit) {
                                    detectVerticalDragGestures(
                                        onDragStart = { offset -> },
                                        onDragEnd = {},
                                        onVerticalDrag = { change, value ->
                                            brightness =
                                                (brightness - (value / density.density)).coerceIn(
                                                    0f,
                                                    100f
                                                )
                                            change.consume()
                                        }
                                    )
                                }
                        ) {
                            AnimatedVisibility(
                                brightnessVisible,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 24.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(96.dp)
                                            .background(Color.White.copy(0.25f), CircleShape),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(4.dp)
                                                .fillMaxHeight(brightness / 100)
                                                .background(Color.White.copy(0.5f), CircleShape)
                                        )
                                    }
                                    Spacer(Modifier.width(16.dp))
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = "Brightness",
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        if (brightness == 0f) "AUTO" else "${brightness.toInt()}%",
                                        style = AppTheme.typography.labelSmallBold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .pointerInput(Unit) {
                                    detectVerticalDragGestures(
                                        onDragStart = { _ -> shouldListen = false },
                                        onDragEnd = { shouldListen = true },
                                        onVerticalDrag = { change, value ->
                                            audio =
                                                (audio - (value / density.density)).coerceIn(
                                                    0f,
                                                    100f
                                                )
                                        }
                                    )
                                }
                        ) {
                            AnimatedVisibility(
                                audioVisible,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 24.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        if (audio == 0f) "AUTO" else "${audio.toInt()}%",
                                        style = AppTheme.typography.labelSmallBold
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Audio",
                                    )
                                    Spacer(Modifier.width(16.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(96.dp)
                                            .background(Color.White.copy(0.25f), CircleShape),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(4.dp)
                                                .fillMaxHeight(audio / 100)
                                                .background(Color.White.copy(0.5f), CircleShape)
                                        )
                                    }
                                }
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
                            if (false)
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