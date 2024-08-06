package com.elfennani.aniwatch.presentation.screens.status.composables

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.elfennani.aniwatch.presentation.screens.status.StatusDateModal
import com.elfennani.aniwatch.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusEditorDatePicker(
    datePickerState: DatePickerState,
    onConfirm: (millis: Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(datePickerState.selectedDateMillis) },
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = AppTheme.colorScheme.primary
                )
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = AppTheme.colorScheme.primary
                )
            ) {
                Text("Cancel")
            }
        },
        colors = DatePickerDefaults.colors(containerColor = AppTheme.colorScheme.primaryContainer)
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors()
                .copy(
                    containerColor = AppTheme.colorScheme.primaryContainer,
                    dayInSelectionRangeContentColor = AppTheme.colorScheme.primary,
                    titleContentColor = AppTheme.colorScheme.primary,
                    selectedDayContainerColor = AppTheme.colorScheme.primary,
                    dayContentColor = AppTheme.colorScheme.onSecondary,
                    weekdayContentColor = AppTheme.colorScheme.onSecondary,
                    headlineContentColor = AppTheme.colorScheme.onSecondary,
                    navigationContentColor = AppTheme.colorScheme.onSecondary,
                    dividerColor = AppTheme.colorScheme.secondary,
                    todayDateBorderColor = AppTheme.colorScheme.primary,
                    todayContentColor = AppTheme.colorScheme.primary,
                    yearContentColor = AppTheme.colorScheme.secondary
                ),
            showModeToggle = false
        )
    }
}