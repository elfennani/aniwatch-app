package com.elfennani.aniwatch.presentation.screens.show.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.presentation.composables.Divider
import com.elfennani.aniwatch.presentation.theme.AppTheme
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@Composable
fun ShowScreenHeader(
    show: ShowDetails,
    lazyListState: LazyListState,
    padding: PaddingValues,
    onBack: () -> Unit,
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

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors()
                    .copy(
                        containerColor = AppTheme.colorScheme.primary,
                        contentColor = AppTheme.colorScheme.onPrimary
                    ),
                shape = AppTheme.shapes.button,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(AppTheme.sizes.small))
                Text(text = "Set Status", style = AppTheme.typography.labelNormal)
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

            Divider()
        }
    }
}