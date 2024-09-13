package com.elfennani.aniwatch.ui.screens.show.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun EpisodeDialog(
    onDismissRequest: () -> Unit,
    episode: Episode,
    onDownload: (EpisodeAudio) -> Unit,
    onDelete: () -> Unit,
    onOpenEpisode: (episode: Double, audio: EpisodeAudio) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                AppTheme.sizes.medium,
                Alignment.CenterVertically
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppTheme.shapes.input)
                    .background(AppTheme.colorScheme.background, AppTheme.shapes.input)
            ) {
                EpisodeCard(
                    episode = episode,
                    minimal = true
                )
            }

            AnimatedVisibility(
                visible = episode.state !is DownloadState.NotSaved,
                modifier = Modifier.fillMaxWidth()
            ) {
                CompositionLocalProvider(LocalContentColor provides AppTheme.colorScheme.onBackground) {
                    AnimatedContent(episode.state::class.simpleName, label = "") { state ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(AppTheme.shapes.input)
                                .background(
                                    AppTheme.colorScheme.background,
                                    AppTheme.shapes.input
                                )
                                .padding(AppTheme.sizes.medium),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
                        ) {
                            when (episode.state) {
                                is DownloadState.Downloaded -> {
                                    Icon(Icons.Default.DownloadDone, contentDescription = null)
                                    Text(
                                        text = "Downloaded (${episode.state.audio})",
                                        style = AppTheme.typography.labelNormal
                                    )
                                }

                                is DownloadState.Downloading -> {
                                    Icon(Icons.Default.Downloading, contentDescription = null)
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            val progressText =
                                                (episode.state.progress * 100).roundToInt()
                                            Text(
                                                text = "Downloading...",
                                                modifier = Modifier.weight(1f),
                                                style = AppTheme.typography.labelNormal,
                                                maxLines = 1
                                            )
                                            Spacer(Modifier.width(AppTheme.sizes.medium))
                                            Text(
                                                "$progressText%",
                                                color = AppTheme.colorScheme.primary,
                                                style = AppTheme.typography.labelNormal.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                        }

                                        LinearProgressIndicator(
                                            progress = episode.state.progress,
                                            modifier = Modifier.fillMaxWidth(),
                                            color = AppTheme.colorScheme.primary
                                        )
                                    }
                                }

                                is DownloadState.Failure -> {
                                    Icon(
                                        Icons.Default.Error,
                                        contentDescription = null,
                                        tint = AppTheme.colorScheme.error
                                    )
                                    Column {
                                        Text(
                                            text = "Download Failed",
                                            style = AppTheme.typography.labelNormal,
                                        )
                                        Spacer(Modifier.height(AppTheme.sizes.smaller))
                                        Text(
                                            text = stringResource(episode.state.message),
                                            style = AppTheme.typography.labelSmall,
                                        )
                                    }
                                }

                                is DownloadState.Pending -> {
                                    Icon(Icons.Default.Downloading, contentDescription = null)
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "Download pending",
                                            style = AppTheme.typography.labelNormal,
                                            maxLines = 1
                                        )
                                        LinearProgressIndicator(
                                            modifier = Modifier.fillMaxWidth(),
                                            color = AppTheme.colorScheme.primary
                                        )
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colorScheme.background, AppTheme.shapes.input)
                    .padding(vertical = AppTheme.sizes.normal)
            ) {
                Text(
                    text = "OPTIONS",
                    style = AppTheme.typography.labelSmallBold,
                    modifier = Modifier.padding(
                        horizontal = AppTheme.sizes.medium,
                        vertical = AppTheme.sizes.small
                    ),
                    color = AppTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.size(AppTheme.sizes.small))

                DialogOption(label = "Watch in SUB", icon = Icons.Default.Audiotrack) {
                    onOpenEpisode(episode.episode, EpisodeAudio.SUB)
                }

                if (episode.dubbed) {
                    DialogOption(label = "Watch in DUB", icon = Icons.Default.Audiotrack) {
                        onOpenEpisode(episode.episode, EpisodeAudio.DUB)
                    }
                }


                if (episode.state is DownloadState.NotSaved || episode.state is DownloadState.Failure) {
                    DialogOption(label = "Download SUB", icon = Icons.Default.DownloadForOffline) {
                        onDownload(EpisodeAudio.SUB)
                        onDismissRequest()
                    }
                    if (episode.dubbed) {
                        DialogOption(
                            label = "Download DUB",
                            icon = Icons.Default.DownloadForOffline
                        ) {
                            onDownload(EpisodeAudio.DUB)
                            onDismissRequest()
                        }
                    }
                }

                if (episode.state is DownloadState.Downloaded) {
                    DialogOption(
                        label = "Delete Download",
                        icon = Icons.Default.Delete,
                        destructive = true
                    ) {
                        onDelete()
                        onDismissRequest()
                    }
                }
            }
        }

    }
}

@Composable
private fun DialogOption(
    label: String,
    icon: ImageVector,
    destructive: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = AppTheme.sizes.medium, horizontal = AppTheme.sizes.medium)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (destructive) AppTheme.colorScheme.error else AppTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = AppTheme.typography.labelNormal,
            color = if (destructive) AppTheme.colorScheme.error else AppTheme.colorScheme.onBackground
        )
    }
}