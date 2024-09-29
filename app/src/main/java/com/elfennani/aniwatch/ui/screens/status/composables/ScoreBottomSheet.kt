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
    initialValue: Double = 0.0,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit,
) {
    var value by remember {
        mutableStateOf(initialValue.toString())
    }

    BottomSheetContainer(
        title = "Set Score",
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(value.toDoubleOrNull() ?: 0.0) }
    ) {
        RangeTextField(
            value = value,
            onValueChange = { value = it },
            max = 100,
            increment = 5
        )
    }
}