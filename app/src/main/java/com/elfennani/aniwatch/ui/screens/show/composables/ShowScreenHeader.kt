package com.elfennani.aniwatch.ui.screens.show.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.ui.composables.Divider
import com.elfennani.aniwatch.ui.theme.AppTheme
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@Composable
fun ShowScreenHeader(
    show: ShowDetails,
    lazyListState: LazyListState,
    padding: PaddingValues,
    isAppendingEpisode: Boolean,
    onStatusClick: () -> Unit,
    onAppendEpisode: () -> Unit,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    var descriptionExpanded by remember { mutableStateOf(false) }
    val richTextState = rememberRichTextState()

    LaunchedEffect(show.description) {
        richTextState.setHtml(show.description)
    }

    Column {
        ShowScreenHeaderBanner(
            banner = show.banner,
            showImage = show.image.original,
            lazyListState = lazyListState,
            padding = padding,
            onBack = onBack,
        )
        Column(
            modifier = Modifier.padding(horizontal = AppTheme.sizes.large),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
        ) {
            ShowGenres(show.genres)
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
            ) {
                Text(
                    text = show.name ?: "",
                    style = AppTheme.typography.titleNormal,
                    color = AppTheme.colorScheme.onBackground
                )
                Text(
                    text = "${show.year} • ${show.season} • ${show.episodesCount} Episodes",
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colorScheme.onSecondary
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal)
            ) {
                StatusButton(
                    modifier = Modifier.weight(1f),
                    status = show.status,
                    progress = show.progress,
                    total = show.episodesCount,
                    onClick = { onStatusClick() }
                )
                if(show.status in listOf(ShowStatus.WATCHING, ShowStatus.REPEATING)){
                    OutlinedButton(
                        onClick = { onAppendEpisode() },
                        colors = ButtonDefaults.outlinedButtonColors()
                            .copy(
                                contentColor = AppTheme.colorScheme.primary,
                            ),
                        shape = AppTheme.shapes.button,
                        border = null,
                        contentPadding = PaddingValues(horizontal = AppTheme.sizes.medium),
                        modifier = Modifier.fillMaxHeight(),
                        enabled = !isAppendingEpisode
                    ) {
                        Text("+ 1 EP")
                    }
                }
            }

            Column {
                Text(
                    text = richTextState.annotatedString,
                    style = AppTheme.typography.labelNormal.copy(fontWeight = FontWeight.Normal),
                    maxLines = if (descriptionExpanded) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                )
                TextButton(
                    modifier = Modifier.offset(x = (-12).dp),
                    onClick = { descriptionExpanded = !descriptionExpanded },
                    colors = ButtonDefaults.textButtonColors().copy(
                        contentColor = AppTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (descriptionExpanded) "Show less" else "Read more",
                        style = AppTheme.typography.labelNormal
                    )
                }
            }
            content()
            Divider()
        }
    }
}