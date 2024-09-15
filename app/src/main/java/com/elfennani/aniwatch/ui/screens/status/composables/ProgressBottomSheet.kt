package com.elfennani.aniwatch.ui.screens.status.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elfennani.aniwatch.ui.theme.AppTheme


@Composable
fun ProgressBottomSheet(
    initialValue: Int = 0,
    total: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    var value by remember {
        mutableStateOf(initialValue.toString())
    }

    BottomSheetContainer(
        title = "Set Progress",
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(value.toIntOrNull() ?: 0) }
    ) {
        RangeTextField(
            value = value,
            onValueChange = { value = it },
            max = total
        )
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
                ProgressBottomSheet(
                    initialValue = 0,
                    total = 13,
                    onDismiss = {},
                    onConfirm = {},
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