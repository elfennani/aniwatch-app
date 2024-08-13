package com.elfennani.aniwatch.presentation.screens.show.composables

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.EpisodeState
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun EpisodeDialog(
    onDismissRequest: () -> Unit,
    episode: Episode,
    onDownload: (EpisodeAudio) -> Unit,
    onDelete: () -> Unit
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
                    .background(AppTheme.colorScheme.background, AppTheme.shapes.input)
            ) {
                EpisodeCard(
                    episode = episode,
                    minimal = true
                )
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
                if (episode.state == EpisodeState.NOT_SAVED) {
                    DialogOption(label = "Download SUB", icon = Icons.Default.DownloadForOffline) {
                        onDownload(EpisodeAudio.SUB)
                        onDismissRequest
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
                if (episode.state == EpisodeState.SAVED) {
                    DialogOption(
                        label = "Delete Download",
                        icon = Icons.Default.Delete,
                        destructive = true
                    ){
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