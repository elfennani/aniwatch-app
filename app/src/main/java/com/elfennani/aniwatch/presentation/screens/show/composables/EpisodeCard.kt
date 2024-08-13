package com.elfennani.aniwatch.presentation.screens.show.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.formatSeconds
import com.elfennani.aniwatch.imageLoader
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.EpisodeState
import com.elfennani.aniwatch.presentation.theme.AppTheme

private const val MAX_LINES_MINIMAL = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeCard(
    modifier: Modifier = Modifier,
    episode: Episode,
    minimal: Boolean = false,
    onOptions: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    val imageLoader = LocalContext.current.imageLoader()
    val subtitle by remember(episode.dubbed, episode.duration) {
        mutableStateOf(episode.duration?.formatSeconds() ?: "")
    }


    Box(modifier = modifier.height(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier
                .combinedClickable(
                    onClick = { onClick() },
                    onLongClick = { onOptions() }
                )
                .fillMaxWidth()
                .padding(
                    horizontal = if (minimal) AppTheme.sizes.normal else AppTheme.sizes.large,
                    vertical = AppTheme.sizes.normal
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = episode.thumbnail,
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
                Text(
                    text = episode.name,
                    style = AppTheme.typography.labelNormal,
                    maxLines = if (minimal) MAX_LINES_MINIMAL else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!subtitle.isNullOrEmpty()) {
                        Text(
                            text = subtitle!!,
                            style = AppTheme.typography.labelSmall,
                            color = AppTheme.colorScheme.onSecondary
                        )
                    }
                    if (!subtitle.isNullOrEmpty() && (episode.state == EpisodeState.SAVED || episode.dubbed)) {
                        BulletSeparator()
                    }
                    if (episode.state == EpisodeState.SAVED) {
                        Icon(
                            imageVector = Icons.Default.FileDownloadDone,
                            contentDescription = null,
                            modifier = Modifier.size(AppTheme.sizes.medium),
                            tint = AppTheme.colorScheme.primary
                        )
                    }
                    if(episode.dubbed && episode.state == EpisodeState.SAVED){
                        BulletSeparator()
                    }
                    if (episode.dubbed) {
                        Text(
                            text = "DUB",
                            style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AppTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (!minimal)
                IconButton(onClick = { onOptions() }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
        }
    }
}

@Composable
private fun BulletSeparator(modifier: Modifier = Modifier) {
    Text(
        text = "•",
        style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        color = AppTheme.colorScheme.onBackground.copy(alpha = 0.33f)
    )
}