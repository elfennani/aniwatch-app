package com.elfennani.aniwatch.presentation.screens.show.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.formatSeconds
import com.elfennani.aniwatch.imageLoader
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun EpisodeCard(
    title: String,
    thumbnail: String?,
    subtitle: String? = null,
    onClick: () -> Unit = {}
) {
    val imageLoader = LocalContext.current.imageLoader()
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
                ),
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
    }
}