package com.elfennani.aniwatch.ui.screens.status.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.elfennani.aniwatch.ui.composables.Button
import com.elfennani.aniwatch.ui.theme.AppTheme
import kotlinx.coroutines.launch

typealias CloseCallback = (() -> Unit)?

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContainer(
    title: String,
    textStyle: TextStyle = AppTheme.typography.labelLarge.copy(textAlign = TextAlign.Center),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = {},
    showButtons: Boolean = true,
    content: @Composable ColumnScope.(requestDismiss: (CloseCallback) -> Unit) -> Unit,
) {
    val isPreview = LocalInspectionMode.current
    val sheetState =
        if (isPreview) rememberStandardBottomSheetState() else rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()

    val onConfirmModalSheet = {
        onConfirm()
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                Log.d("BottomSheetContainer", "BottomSheetContainer: log")
                onDismiss()
            }
        }
    }

    val onCloseModalSheet: (CloseCallback) -> Unit = { callback ->
        callback?.invoke()
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = AppTheme.sizes.large)
                .padding(bottom = AppTheme.sizes.large),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.large)
        ) {
            Text(
                text = title,
                style = textStyle,
                modifier = Modifier
                    .fillMaxWidth()
            )
            content(onCloseModalSheet)
            if (showButtons) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onCloseModalSheet(null) },
                        textButton = true
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(modifier = Modifier.weight(1f), onClick = { onConfirmModalSheet() }) {
                        Text(text = "Confirm")
                    }
                }
            }
        }

    }
}