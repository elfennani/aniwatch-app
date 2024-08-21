package com.elfennani.aniwatch.presentation.screens.show.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun ShowGenres(genres: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.small),
        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)
    ) {
        genres.map {
            Text(
                text = it,
                color = AppTheme.colorScheme.onPrimary,
                style = AppTheme.typography.labelSmallBold,
                modifier = Modifier
                    .background(
                        AppTheme.colorScheme.primary,
                        RoundedCornerShape(4.dp)
                    )
                    .padding(
                        vertical = AppTheme.sizes.smaller * 1.25f,
                        horizontal = AppTheme.sizes.small
                    )
            )
        }
    }
}