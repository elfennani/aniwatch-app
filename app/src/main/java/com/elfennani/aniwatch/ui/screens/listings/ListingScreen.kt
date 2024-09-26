package com.elfennani.aniwatch.ui.screens.listings

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.models.enums.formatText
import com.elfennani.aniwatch.ui.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.ui.composables.ShowCard
import com.elfennani.aniwatch.ui.screens.show.ShowRoute
import com.elfennani.aniwatch.ui.screens.status.composables.StatusBottomSheet
import com.elfennani.aniwatch.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingScreen(
    state: ListingUiState,
    onClickShow: (id: Int) -> Unit,
    onSelectStatus: (ShowStatus) -> Unit,
    onErrorDismiss: (Int) -> Unit,
) {
    var statusSelector by remember {
        mutableStateOf(false)
    }

    Scaffold(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        snackbarHost = { ErrorSnackbarHost(errors = state.errors, onErrorDismiss) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Listing",
                        style = AppTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = AppTheme.colorScheme.card,
                    titleContentColor = AppTheme.colorScheme.onCard
                ),
                actions = {
                    TextButton(
                        onClick = { statusSelector = true},
                        colors = ButtonDefaults.textButtonColors().copy(
                            contentColor = AppTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = state.status.formatText())
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars.add(
            WindowInsets.navigationBars.add(
                WindowInsets(bottom = 64.dp)
            )
        )
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(
                items = state.listing,
                key = { it.id }
            ) {
                ShowCard(show = it, onClick = { onClickShow(it.id) })
            }
        }

        if (statusSelector)
            StatusBottomSheet(selected = state.status, onDismiss = { statusSelector = false }) {
                onSelectStatus(it)
            }
    }
}

const val LISTING_SCREEN_PATTERN = "listing"
fun NavGraphBuilder.listingScreen(navController: NavController) {
    composable(
        LISTING_SCREEN_PATTERN,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        val viewModel: ListingViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        ListingScreen(
            state = state,
            onClickShow = { navController.navigate(ShowRoute(it)) },
            onSelectStatus = viewModel::setStatus,
            onErrorDismiss = viewModel::dismissError
        )
    }
}

fun NavController.navigateToListingScreen(popUpToTop: Boolean = false) {
    this.navigate(LISTING_SCREEN_PATTERN) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}
