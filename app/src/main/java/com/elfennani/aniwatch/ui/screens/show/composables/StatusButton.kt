package com.elfennani.aniwatch.ui.screens.show.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.formatText
import com.elfennani.aniwatch.models.toIcon
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
private fun StatusButtonContainer(
    modifier: Modifier = Modifier,
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
            modifier = modifier,
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
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            content()
        }
}

@Composable
fun StatusButton(
    modifier: Modifier = Modifier,
    status: ShowStatus?,
    progress: Int? = null,
    total: Int? = null,
    onClick: () -> Unit = {},
) {
    val text = status.formatText(progress, total)
    val icon = status.toIcon()

    StatusButtonContainer(
        modifier = modifier,
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