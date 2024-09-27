package com.elfennani.aniwatch.ui.screens.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.utils.imageLoader
import com.elfennani.aniwatch.domain.models.ShowImage
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun WatchingCard(show: Show, onPress: () -> Unit = {}) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .clip(AppTheme.shapes.thumbnail)
            .width(256.dp)
            .clickable { onPress() },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio((21 / 9f))
                .clip(AppTheme.shapes.thumbnail)
                .background(AppTheme.colorScheme.secondary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = show.banner ?: show.image.original,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                imageLoader = context.imageLoader(),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .background(Color.Black.copy(0.1f))
                    .fillMaxSize()
                    .padding(AppTheme.sizes.small),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = "Episode ${show.progress!! + 1}",
                    style = AppTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }

        Text(
            text = show.name,
            style = AppTheme.typography.labelNormal.copy(lineHeight = 18.sp),
            modifier = Modifier.padding(AppTheme.sizes.small),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}