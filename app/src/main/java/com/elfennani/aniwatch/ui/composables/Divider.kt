package com.elfennani.aniwatch.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.onBackground.copy(.07f),
) {
    androidx.compose.material.Divider(modifier = modifier, color = color)
}