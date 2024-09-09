package com.elfennani.aniwatch.ui.screens.status.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun Setting(
    label: String,
    value: String? = null,
    onClear: (() -> Unit)? = null,
    onClick: (() -> Unit) = {},
) {
    Row(
        modifier = Modifier
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = AppTheme.typography.labelSmall,
            modifier = Modifier.weight(1f)
        )
        AnimatedContent(targetState = value, label = "", modifier = Modifier.weight(1f)) { value ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value ?: "-",
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                val clearButtonEnabled = onClear != null && !value.isNullOrEmpty()
                IconButton(
                    enabled = clearButtonEnabled,
                    onClick = { onClear?.invoke() },
                    modifier = Modifier
                        .size(32.dp)
                        .alpha(if (clearButtonEnabled) 1f else 0f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}