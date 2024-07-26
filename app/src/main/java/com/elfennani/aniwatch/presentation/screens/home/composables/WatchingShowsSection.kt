package com.elfennani.aniwatch.presentation.screens.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.presentation.composables.Section
import com.elfennani.aniwatch.presentation.composables.WatchingCard
import com.elfennani.aniwatch.presentation.screens.show.navigateToShowScreen
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun WatchingShowsSection(
    onPressShow: (showId: Int) -> Unit = {},
    shows: List<ShowBasic>,
) {
    Section(
        title = "Continue Watching",
        modifier = Modifier
            .padding(horizontal = AppTheme.sizes.medium)
            .padding(bottom = AppTheme.sizes.medium)
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
        contentPadding = PaddingValues(horizontal = AppTheme.sizes.medium),
    ) {
        items(shows) { show ->
            WatchingCard(
                show = show,
                onPress = { onPressShow(show.id) }
            )
        }
    }
}