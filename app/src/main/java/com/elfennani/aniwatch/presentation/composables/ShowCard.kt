package com.elfennani.aniwatch.presentation.composables

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.imageLoader
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.formatText
import com.elfennani.aniwatch.models.toIcon
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun ShowCard(
    show: ShowBasic,
    onClick: () -> Unit = {}
) {
    val imageLoader = LocalContext.current.imageLoader()
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(horizontal = AppTheme.sizes.large, vertical = AppTheme.sizes.normal),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
    ) {
        AsyncImage(
            model = show.image.large,
            contentDescription = null,
            imageLoader = imageLoader,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(80.dp)
                .clip(AppTheme.shapes.thumbnail)
                .background(AppTheme.colorScheme.secondary.copy(alpha = 0.1f))
                .aspectRatio(0.69f)
        )
        Column(
            modifier = Modifier
                .padding(vertical = AppTheme.sizes.normal),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
        ) {
            Text(text = show.name, style = AppTheme.typography.labelLarge, maxLines = 2)
            Text(text = "${show.episodes} Episodes", style = AppTheme.typography.labelSmall, color = AppTheme.colorScheme.onSecondary)
            Spacer(modifier = Modifier.height(AppTheme.sizes.small))
            if(show.status != null){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)
                ) {
                    Icon(
                        imageVector = show.status.toIcon(),
                        contentDescription = null,
                        tint = AppTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = show.status.formatText(),
                        style = AppTheme.typography.labelNormal,
                        color = AppTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun ShowCardPrev() {
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
        Scaffold(
            containerColor = AppTheme.colorScheme.background,
            contentColor = AppTheme.colorScheme.onBackground,
            modifier = Modifier.height(200.dp)
        ) {
            ShowCard(show = fakeShow)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShowCardPrevDark() {
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
        Scaffold(
            containerColor = AppTheme.colorScheme.background,
            contentColor = AppTheme.colorScheme.onBackground,
            modifier = Modifier.height(200.dp)
        ) {
            ShowCard(show = fakeShow)
        }
    }
}