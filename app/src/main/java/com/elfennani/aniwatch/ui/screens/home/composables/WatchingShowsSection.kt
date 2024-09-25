package com.elfennani.aniwatch.ui.screens.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.ui.composables.Section
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun WatchingShowsSection(
    onPressShow: (showId: Int) -> Unit = {},
    shows: List<ShowBasic>,
    isLoading: Boolean = false,
) {
    val isEmpty = !isLoading && shows.isEmpty();

    Section(
        title = "Continue Watching",
        modifier = Modifier
            .padding(horizontal = AppTheme.sizes.medium)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
            contentPadding = PaddingValues(horizontal = AppTheme.sizes.medium),
            userScrollEnabled = !isLoading && shows.isNotEmpty(),
            modifier = Modifier.emptyFade(isEmpty)
        ) {
            if (isLoading) {
                item {
                    WatchingCardSkeleton()
                }
            }
            if (isEmpty) {
                item { WatchingCardSkeleton(animated = false) }
                item { WatchingCardSkeleton(animated = false) }
            }
            items(shows, key = { it.id }) { show ->
                WatchingCard(
                    show = show,
                    onPress = { onPressShow(show.id) }
                )
            }
        }

        if (isEmpty) {
            Column(
                modifier = Modifier.matchParentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nothing to see",
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun Modifier.emptyFade(enabled: Boolean = false): Modifier {
    val background = AppTheme.colorScheme.background

    if (enabled) {
        return this
            .alpha(0.25f)
            .drawWithContent {
                val colors = listOf(Color.Transparent, background)
                drawContent()
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors,
                        startX = Float.POSITIVE_INFINITY,
                        endX = 0f
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
    }

    return this
}