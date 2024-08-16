package com.elfennani.aniwatch.presentation.screens.home.composables

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
import coil.ImageLoader
import coil.compose.AsyncImage
import com.elfennani.aniwatch.imageLoader
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun WatchingCard(show: ShowBasic, onPress: () -> Unit = {}) {
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

@Preview
@Composable
fun WatchingCardPreview() {
    val fakeShow = ShowBasic(
        id = 100077,
        name = "Sound Euphonium S3",
        status = ShowStatus.WATCHING,
        progress = 8,
        description = "The comedy manga centers around a super-powered girl named Hina and Nitta, a young member of the yakuza. Hina suddenly appears in Nitta's room and threatens him with her extraordinary powers. However, they end up living together.<br><br>\\n\\n(Source: Anime News Network)",
        episodes = 12,
        image = ShowImage(
            large = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx100077-B4bfXz0wVOPO.jpg",
            medium = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx100077-B4bfXz0wVOPO.jpg",
            original = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx100077-B4bfXz0wVOPO.jpg",
            color = Color(0xFFe47850)
        ),
        banner = null,
        updatedAt = null
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