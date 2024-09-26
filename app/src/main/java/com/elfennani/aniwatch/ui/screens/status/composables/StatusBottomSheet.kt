package com.elfennani.aniwatch.ui.screens.status.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.models.enums.formatText
import com.elfennani.aniwatch.domain.models.enums.toIcon
import com.elfennani.aniwatch.ui.composables.Button
import com.elfennani.aniwatch.ui.theme.AppTheme


@Composable
fun StatusBottomSheet(
    selected: ShowStatus?,
    onDismiss: () -> Unit,
    onConfirm: (ShowStatus) -> Unit,
) {
    BottomSheetContainer(
        title = "Select Status",
        textStyle = AppTheme.typography.labelLarge,
        onDismiss = { onDismiss() },
        showButtons = false
    ) {requestDismiss ->
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal)
        ){
            ShowStatus.entries.forEach {
                Button(
                    enabled = it != selected,
                    textButton = true,
                    onClick = { requestDismiss { onConfirm(it) } },
                ) {
                    Icon(imageVector = it.toIcon(), contentDescription = null)
                    Spacer(modifier = Modifier.width(AppTheme.sizes.medium))
                    Text(text = it.formatText(), modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScoreBottomSheetPreview() {
    AppTheme {
        Scaffold(
            containerColor = AppTheme.colorScheme.background,
            contentColor = AppTheme.colorScheme.onBackground
        ) {
            Box(
                modifier = Modifier.padding(it)
            ) {
                StatusBottomSheet(
                    selected = ShowStatus.WATCHING,
                    onDismiss = {},
                    onConfirm = {}
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScoreBottomSheetPreviewDark() {
    ScoreBottomSheetPreview()
}