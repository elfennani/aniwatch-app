package com.elfennani.aniwatch.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun WatchingCard(show: ShowBasic, onPress: () -> Unit = {}) {
    val nextEpisode = (show.progress ?: 0) + 1
    Column(
        modifier = Modifier
            .clip(AppTheme.shapes.card)
            .background(AppTheme.colorScheme.card, AppTheme.shapes.card)
            .clickable { onPress() },
    ) {
        Box(
            modifier = Modifier
                .aspectRatio((16 / 9f))
                .clip(AppTheme.shapes.card)
                .background(AppTheme.colorScheme.onSecondary)
                ,
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = show.image.large,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(AppTheme.sizes.normal),
                contentScale = ContentScale.FillWidth,
            )
            Box(
                modifier = Modifier
                    .background(AppTheme.colorScheme.background.copy(0.1f))
                    .fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.25f), CircleShape)
                    .padding(AppTheme.sizes.small)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Column(
            modifier = Modifier.padding(AppTheme.sizes.large),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
        ) {
            Text(text = show.name, style = AppTheme.typography.titleNormal)
            Text(
                text = "Watch Episode $nextEpisode",
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colorScheme.onSecondary
            )
        }
    }
}

@Preview
@Composable
fun WatchingCardPreview() {
    val fakeShow = ShowBasic(
        id = 100077,
        name = "HINAMATSURI",
        status = ShowStatus.WATCHING,
        progress = 8,
        description = "The comedy manga centers around a super-powered girl named Hina and Nitta, a young member of the yakuza. Hina suddenly appears in Nitta's room and threatens him with her extraordinary powers. However, they end up living together.<br><br>\\n\\n(Source: Anime News Network)",
        episodes = 12,
        image = ShowImage(
            large = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx100077-B4bfXz0wVOPO.jpg",
            medium = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx100077-B4bfXz0wVOPO.jpg",
            original = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx100077-B4bfXz0wVOPO.jpg",
            color = Color(0xFFe47850)
        )
    )

    AppTheme {
        Column(
            modifier = Modifier
                .background(AppTheme.colorScheme.background)
                .padding(AppTheme.sizes.normal)
                .width(411.dp)
        ) {
            WatchingCard(show = fakeShow)
        }
    }
}