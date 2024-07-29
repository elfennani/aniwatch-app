package com.elfennani.aniwatch.presentation.screens.home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.presentation.composables.SearchBoxButton
import com.elfennani.aniwatch.presentation.theme.AppTheme

private const val DIVIDER_FADE_DURATION = 150

@Composable
fun HomeHeader(modifier: Modifier = Modifier, border: Boolean = false) {
    val logoResource = with(isSystemInDarkTheme()) {
        if (this)
            R.drawable.aniwatch_dark_mode
        else
            R.drawable.aniwatch_light_mode
    }


    Column {
        Column(
            modifier = Modifier
                .padding(AppTheme.sizes.medium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
        ) {
            Image(
                painter = painterResource(id = logoResource),
                contentDescription = null,
                modifier = Modifier.height(AppTheme.sizes.medium)
            )
            SearchBoxButton()
        }
        AnimatedVisibility(
            visible = border,
            enter = fadeIn(tween(DIVIDER_FADE_DURATION)),
            exit = fadeOut(tween(DIVIDER_FADE_DURATION))
        ) {
            Divider(color = AppTheme.colorScheme.onBackground.copy(.07f))
        }
    }
}

