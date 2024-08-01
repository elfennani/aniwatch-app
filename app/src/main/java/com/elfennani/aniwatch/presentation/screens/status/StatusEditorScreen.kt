package com.elfennani.aniwatch.presentation.screens.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.StatusDate
import com.elfennani.aniwatch.models.StatusDetails
import com.elfennani.aniwatch.presentation.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.presentation.composables.dummyShow
import com.elfennani.aniwatch.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusEditorScreen(
    state: StatusEditorUiState,
    onBack: () -> Unit,
    onErrorDismiss: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.primaryContainer,
                ),
                title = { Text(text = "Edit Show Status") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "back",
                            tint = Color(0xFFef4444)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            ErrorSnackbarHost(errors = state.errors) {
                onErrorDismiss(it)
            }
        },
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            if (state.isPending) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(AppTheme.sizes.large * 2.5f))
                }
            } else {

            }
        }
    }
}

@Preview
@Composable
private fun ShowEditorScreenPreview() {
    AppTheme {
        StatusEditorScreen(
            state = StatusEditorUiState(
                isPending = false,
                show = dummyShow,
                status = StatusDetails(
                    status = ShowStatus.WATCHING,
                    score = 100,
                    progress = 12,
                    favorite = false,
                    startedAt = StatusDate(year = 2024, month = 6, day = 29),
                    completedAt = StatusDate(year = 2024, month = 7, day = 1)
                )
            ),
            onBack = {},
            onErrorDismiss = {},
        )
    }
}

const val STATUS_EDITOR_SCREEN_PATTERN = "status/editor/{id}"
fun NavGraphBuilder.statusEditorScreen(navController: NavController) {
    composable(
        route = STATUS_EDITOR_SCREEN_PATTERN,
        arguments = listOf(navArgument("id") { type = NavType.IntType }),
    ) {
        val viewModel: StatusEditorViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        StatusEditorScreen(
            state = state,
            onBack = { navController.popBackStack() },
            onErrorDismiss = viewModel::dismissError
        )
    }
}

fun NavController.navigateToStatusEditorScreen(id: Int, popUpToTop: Boolean = false) {
    this.navigate(
        route = STATUS_EDITOR_SCREEN_PATTERN
            .replace("{id}", id.toString())
    ) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}