package com.elfennani.aniwatch.ui.screens.home.composables

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.utils.imageLoader
import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.ActivityType
import com.elfennani.aniwatch.models.ActivtyShow
import com.elfennani.aniwatch.models.MediaType
import com.elfennani.aniwatch.ui.theme.AppTheme
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityCard(modifier: Modifier = Modifier, activity: Activity, onClick: () -> Unit = {}) {
    val formattedDateTime by remember {
        derivedStateOf {
            val dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT)
            val timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT)

            val formattedDate = dateFormatter.format(activity.createdAt)
            val formattedTime = timeFormatter.format(activity.createdAt)

            "$formattedDate $formattedTime"
        }
    }

    val content by remember {
        derivedStateOf {
            activity.content.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .clickable { onClick() }
//            .background(AppTheme.colorScheme.card)
            .padding(AppTheme.sizes.medium)
            .height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal)
        ) {
            AsyncImage(
                model = activity.userAvatar,
                contentDescription = null,
                modifier = Modifier
                    .size(AppTheme.sizes.large * 1.5f)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                imageLoader = context.imageLoader()
            )

            Column(
                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
            ) {
                Text(
                    text = activity.userName,
                    style = AppTheme.typography.labelLarge,
                    color = AppTheme.colorScheme.onBackground
                )
                Text(
                    text = formattedDateTime,
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colorScheme.onSecondary
                )
            }
        }
        Text(
            text = content,
            style = AppTheme.typography.body,
        )
        if (activity.show != null) {
            Row {
                AsyncImage(
                    model = activity.show.image,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(AppTheme.shapes.thumbnail)
                        .width(64.dp)
                        .aspectRatio(0.69f)
                        .height(IntrinsicSize.Min),
                    contentScale = ContentScale.Crop,
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = AppTheme.sizes.small)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller),
                    ) {
                        Text(text = activity.show.name, style = AppTheme.typography.labelNormal)
                        Text(
                            text = activity.show.type.toString(),
                            style = AppTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = AppTheme.colorScheme.onSecondary
                        )
                        if (activity.show.year != null) {
                            Text(
                                text = activity.show.year.toString(),
                                style = AppTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = AppTheme.colorScheme.onSecondary
                            )
                        }
                    }

                    InfoRow(activity)
                }
            }
        } else {
            InfoRow(activity)
        }
    }
}

@Composable
private fun InfoRow(activity: Activity) {
    if (activity.likes != 0 || activity.replies != 0) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium, Alignment.End),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
            ) {
                Text(
                    text = activity.likes.toString(),
                    style = AppTheme.typography.labelNormal,
                    color = AppTheme.colorScheme.secondary
                )
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = AppTheme.colorScheme.secondary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
            ) {
                Text(
                    text = activity.replies.toString(),
                    style = AppTheme.typography.labelNormal,
                    color = AppTheme.colorScheme.secondary
                )
                Icon(
                    imageVector = Icons.Default.ChatBubble,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = AppTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview
@Composable
private fun ActivityCardPreview() {
    val activity = Activity(
        id = 765110203,
        content = "watched episode 1 of Mushoku Tensei: Jobless Reincarnation",
        type = ActivityType.LIST,
        userId = 863261,
        userName = "Elfennani",
        userAvatar = "https://s4.anilist.co/file/anilistcdn/user/avatar/medium/b863261-VHMHjX3I0lLu.png",
        createdAt = Date(1722011555 * 1000L),
        likes = 1,
        replies = 3,
        show = ActivtyShow(
            id = 108465,
            name = "Mushoku Tensei: Jobless Reincarnation",
            image = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx108465-B9S9zC68eS5j.jpg",
            type = MediaType.ANIME,
            year = 2021
        ),
    )

    AppTheme {
        Surface(
            contentColor = AppTheme.colorScheme.onBackground,
            color = AppTheme.colorScheme.background,
        ) {
            Column {
                ActivityCard(
                    activity = activity
                )
            }
        }
    }
}

@Preview
@Composable
private fun ActivityCardPreview2() {
    val activity = Activity(
        id = 765110203,
        content = "watched episode 1 of Mushoku Tensei: Jobless Reincarnation",
        type = ActivityType.LIST,
        userId = 863261,
        userName = "Elfennani",
        userAvatar = "https://s4.anilist.co/file/anilistcdn/user/avatar/medium/b863261-VHMHjX3I0lLu.png",
        createdAt = Date(1722011555 * 1000L),
        likes = 1,
        replies = 3,
        show = null
    )

    AppTheme {
        Surface(
            contentColor = AppTheme.colorScheme.onBackground,
            color = AppTheme.colorScheme.background,
        ) {
            Column {
                ActivityCard(
                    activity = activity
                )
            }
        }
    }
}