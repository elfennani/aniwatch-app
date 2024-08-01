package com.elfennani.aniwatch.presentation.screens.show.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PausePresentation
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
private fun StatusButtonContainer(
    outline: Boolean = false,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (outline) {
        OutlinedButton(
            onClick = { onClick() },
            colors = ButtonDefaults.outlinedButtonColors()
                .copy(
                    contentColor = AppTheme.colorScheme.primary,
                ),
            shape = AppTheme.shapes.button,
            border = null,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            content()
        }
    } else
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors()
                .copy(
                    containerColor = AppTheme.colorScheme.primary,
                    contentColor = AppTheme.colorScheme.onPrimary
                ),
            shape = AppTheme.shapes.button,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            content()
        }
}

@Composable
fun StatusButton(
    status: ShowStatus?,
    progress: Int? = null,
    total: Int? = null,
    onClick: () -> Unit = {},
) {
    val text = when (status) {
        ShowStatus.ON_HOLD -> "Paused"
        ShowStatus.DROPPED -> "Dropped"
        ShowStatus.REPEATING -> "Rewatching"
        ShowStatus.WATCHING -> {
            if (progress != null && total != null)
                "Currently Watching $progress/$total"
            else
                "Currently Watching"
        }

        ShowStatus.COMPLETED -> "Finished"
        ShowStatus.PLAN_TO_WATCH -> "Planned"
        else -> "Set Status"
    }

    val icon = when (status) {
        ShowStatus.WATCHING -> Icons.Default.SmartDisplay
        ShowStatus.ON_HOLD -> Icons.Default.PausePresentation
        ShowStatus.DROPPED -> Icons.Default.PlaylistRemove
        ShowStatus.PLAN_TO_WATCH -> Icons.AutoMirrored.Default.PlaylistAdd
        ShowStatus.COMPLETED -> Icons.Default.Done
        ShowStatus.REPEATING -> Icons.Default.Repeat
        else -> Icons.Default.Add
    }

    StatusButtonContainer(
        onClick = { onClick() },
        outline = status == null
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(AppTheme.sizes.small))
        Text(text = text, style = AppTheme.typography.labelNormal)
    }
}

@Composable
fun StatusButtonPreviewContainer(status: ShowStatus?) {
    AppTheme {
        Surface(
            color = AppTheme.colorScheme.background,
            contentColor = AppTheme.colorScheme.onBackground,

            ) {
            Box(
                modifier = Modifier
                    .padding(AppTheme.sizes.medium)
                    .width(411.dp)
            ) {
                StatusButton(status = status, progress = 9, total = 13)
            }
        }
    }
}

@Preview
@Composable
private fun StatusButtonPrevNone() {
    StatusButtonPreviewContainer(status = null)
}

@Preview
@Composable
private fun StatusButtonPrevWatching() {
    StatusButtonPreviewContainer(status = ShowStatus.WATCHING)
}

@Preview
@Composable
private fun StatusButtonPrevCompleted() {
    StatusButtonPreviewContainer(status = ShowStatus.COMPLETED)
}

@Preview
@Composable
private fun StatusButtonPrevPlanned() {
    StatusButtonPreviewContainer(status = ShowStatus.PLAN_TO_WATCH)
}

@Preview
@Composable
private fun StatusButtonPrevDropped() {
    StatusButtonPreviewContainer(status = ShowStatus.DROPPED)
}

@Preview
@Composable
private fun StatusButtonPrevRepeating() {
    StatusButtonPreviewContainer(status = ShowStatus.REPEATING)
}

@Preview
@Composable
private fun StatusButtonPrevOnHold() {
    StatusButtonPreviewContainer(status = ShowStatus.ON_HOLD)
}