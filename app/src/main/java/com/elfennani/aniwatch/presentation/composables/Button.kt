package com.elfennani.aniwatch.presentation.composables

import androidx.collection.emptyLongSet
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    textButton: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    if (textButton) {
        OutlinedButton(
            onClick = { onClick() },
            colors = ButtonDefaults.outlinedButtonColors()
                .copy(
                    contentColor = AppTheme.colorScheme.primary,
                    disabledContentColor = AppTheme.colorScheme.secondary
                ),
            shape = AppTheme.shapes.button,
            border = null,
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            enabled = enabled
        ) {
            content()
        }
    } else {
        androidx.compose.material3.Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors()
                .copy(
                    containerColor = AppTheme.colorScheme.primary,
                    contentColor = AppTheme.colorScheme.onPrimary,
                    disabledContainerColor = AppTheme.colorScheme.secondary
                ),
            shape = AppTheme.shapes.button,
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            enabled = enabled
        ) {
            content()
        }
    }
}