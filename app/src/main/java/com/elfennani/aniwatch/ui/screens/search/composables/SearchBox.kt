package com.elfennani.aniwatch.ui.screens.search.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun SearchBox(
    query: String,
    onQueryChanged: (String) -> Unit,
) {
    val focuser = remember {
        FocusRequester()
    }
    val textSelectionColors = TextSelectionColors(
        handleColor = AppTheme.colorScheme.primary,
        backgroundColor = AppTheme.colorScheme.primary.copy(alpha = 0.4f)
    )

    LaunchedEffect(key1 = Unit) {
        focuser.requestFocus()
    }

    Column(
        modifier = Modifier
            .background(AppTheme.colorScheme.card)
            .fillMaxWidth()
            .padding(
                top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding()
            )
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                textStyle = AppTheme.typography.body.copy(
                    color = AppTheme.colorScheme.onBackground,
                ),
                cursorBrush = SolidColor(AppTheme.colorScheme.primary),
                modifier = Modifier.focusRequester(focuser),
                decorationBox = {
                    Row(
                        modifier = Modifier
                            .padding(
                                vertical = AppTheme.sizes.medium,
                                horizontal = AppTheme.sizes.medium
                            )
                            .padding(top = AppTheme.sizes.small),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = AppTheme.colorScheme.secondary
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            if (query.isEmpty()) {
                                Text(
                                    text = "Attack on Titan...",
                                    modifier = Modifier
                                        .matchParentSize()
                                        .alpha(0.16f),
                                    style = AppTheme.typography.body.copy(
                                        color = AppTheme.colorScheme.onBackground,
                                    )
                                )
                            }
                            it()
                        }
                    }
                }
            )
        }
    }
}