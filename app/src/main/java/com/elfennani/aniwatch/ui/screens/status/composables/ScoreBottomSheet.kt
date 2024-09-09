package com.elfennani.aniwatch.ui.screens.status.composables

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
fun ScoreBottomSheet(
    initialValue: Int = 0,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    var value by remember {
        mutableStateOf(initialValue.toString())
    }

    BottomSheetContainer(
        title = "Set Score",
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(value.toIntOrNull() ?: 0) }
    ) {
        RangeTextField(
            value = value,
            onValueChange = { value = it },
            max = 100,
            increment = 5
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
                ScoreBottomSheet(
                    initialValue = 0,
                    onDismiss = {},
                    onConfirm = {}
                )
            }
        }
    }
}