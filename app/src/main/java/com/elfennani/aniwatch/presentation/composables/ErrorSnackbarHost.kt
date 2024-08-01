package com.elfennani.aniwatch.presentation.composables

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun ErrorSnackbarHost(
    errors: List<Int>,
    onErrorDismiss: (errorId:Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (errors.isNotEmpty()) {
        val errorMessage = stringResource(id = errors.first())
        LaunchedEffect(errorMessage, snackbarHostState) {
            snackbarHostState.showSnackbar(message = errorMessage)
            onErrorDismiss(errors.first())
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}