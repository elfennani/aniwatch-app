package com.elfennani.aniwatch.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.onBackground.copy(.07f),
) {
    androidx.compose.material.Divider(modifier = modifier, color = color)
}