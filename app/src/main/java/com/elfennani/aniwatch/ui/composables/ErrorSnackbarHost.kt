package com.elfennani.aniwatch.ui.composables

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource

@Composable
fun ErrorSnackbarHost(
    errors: List<Int>,
    onErrorDismiss: (errorId: Int) -> Unit,
    actionName: String? = null,
    onPressAction: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (errors.isNotEmpty()) {
        val errorMessage = stringResource(id = errors.first())
        LaunchedEffect(errorMessage, snackbarHostState) {
            val snackBarResult =
                snackbarHostState.showSnackbar(message = errorMessage, actionLabel = actionName)
            if (snackBarResult == SnackbarResult.ActionPerformed) {
                onPressAction()
            }
            onErrorDismiss(errors.first())
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}