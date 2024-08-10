package com.elfennani.aniwatch.presentation.screens.show.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.imageLoader
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.EpisodeState
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun EpisodeCard(
    title: String,
    thumbnail: String?,
    subtitle: String? = null,
    dubbed: Boolean,
    episodeState: EpisodeState,
    onDownload: (EpisodeAudio) -> Unit = {},
    onClick: () -> Unit = {},
) {
    val imageLoader = LocalContext.current.imageLoader()
    var audioSelector by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.sizes.large,
                    vertical = AppTheme.sizes.normal
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = thumbnail,
                contentDescription = null,
                imageLoader = imageLoader,
                modifier = Modifier
                    .clip(AppTheme.shapes.thumbnail)
                    .align(Alignment.Top)
                    .background(AppTheme.colorScheme.secondary.copy(0.25f))
                    .width(128.dp)
                    .aspectRatio(16 / 9f)
            )
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = AppTheme.sizes.normal,
                        vertical = AppTheme.sizes.smaller
                    )
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    AppTheme.sizes.smaller,
                    Alignment.CenterVertically
                )
            ) {
                Text(text = title, style = AppTheme.typography.labelNormal)
                if (!subtitle.isNullOrEmpty()) {
                    Text(
                        text = subtitle,
                        style = AppTheme.typography.labelSmall,
                        color = AppTheme.colorScheme.onSecondary
                    )
                }
            }
            when (episodeState) {
                EpisodeState.NOT_SAVED -> {
                    IconButton(onClick = {
                        if (dubbed) audioSelector = true
                        else onDownload(EpisodeAudio.SUB)
                    }) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null)
                    }
                }

                EpisodeState.SAVED -> {
                    IconButton(
                        onClick = {},
                        enabled = false,
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            disabledContentColor = AppTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownloadDone,
                            contentDescription = null
                        )
                    }
                }

                else -> {}
            }
        }

        AnimatedVisibility(
            visible = audioSelector,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .clickable { audioSelector = false }
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
            )
        }

        AnimatedVisibility(
            visible = audioSelector,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    AppTheme.sizes.small,
                    Alignment.CenterHorizontally
                )
            ) {
                Button(
                    onClick = { onDownload(EpisodeAudio.SUB) },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = AppTheme.colorScheme.primary,
                        contentColor = AppTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "SUB")
                }
                Button(
                    onClick = { onDownload(EpisodeAudio.DUB) },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = AppTheme.colorScheme.primary,
                        contentColor = AppTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "DUB")
                }
                FilledIconButton(
                    onClick = { audioSelector = false },
                    colors = IconButtonDefaults.filledIconButtonColors().copy(
                        containerColor = AppTheme.colorScheme.primary,
                        contentColor = AppTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EpisodeCardPreview() {
    AppTheme {
        Scaffold(
            modifier = Modifier.height(128.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EpisodeCard(
                    title = "Episode 5 - The wild hunt",
                    thumbnail = null,
                    subtitle = "hello world",
                    dubbed = true,
                    episodeState = EpisodeState.UNKNOWN,
                    onClick = {}
                )
            }
        }
    }
}