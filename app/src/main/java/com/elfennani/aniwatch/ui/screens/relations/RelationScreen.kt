package com.elfennani.aniwatch.ui.screens.relations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elfennani.aniwatch.domain.models.enums.RelationType.Companion.value
import com.elfennani.aniwatch.ui.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.ui.composables.ShowCard
import com.elfennani.aniwatch.ui.screens.show.ShowRoute
import com.elfennani.aniwatch.ui.theme.AppTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelationScreen(
    state: RelationUiState,
    onOpenShow: (Int) -> Unit,
    onErrorDismiss: (Int) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        snackbarHost = {
            ErrorSnackbarHost(
                errors = state.errors,
                onErrorDismiss = onErrorDismiss
            )
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = AppTheme.colorScheme.card,
                    titleContentColor = AppTheme.colorScheme.onCard,
                    navigationIconContentColor = AppTheme.colorScheme.onCard,
                    actionIconContentColor = AppTheme.colorScheme.onCard
                ),
                title = {
                    Text(text = "Relations", style = AppTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        if (!state.isLoading) {
            LazyColumn(contentPadding = paddingValues) {
                items(state.relations) { (relationType, show) ->
                    ShowCard(
                        show = show,
                        subtitle = "${show.format.value} • ${show.state?.value}",
                        overlay = {
                            Box(
                                modifier = Modifier
                                    .padding(AppTheme.sizes.small)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color.Black.copy(alpha = 0.2f))
                                    .padding(AppTheme.sizes.smaller)
                            ) {
                                Text(
                                    text = relationType.value,
                                    style = AppTheme.typography.labelSmallBold,
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    modifier = Modifier,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        },
                        onClick = { onOpenShow(show.id) }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = AppTheme.colorScheme.primary)
            }
        }
    }
}

private fun String.formatCapitalize() = this
    .lowercase()
    .replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else
            it.toString()
    }

const val RELATION_SCREEN_PATTERN = "relation/{showId}"
fun NavGraphBuilder.relationScreen(navController: NavController) {
    composable(
        route = RELATION_SCREEN_PATTERN,
        arguments = listOf(navArgument("showId") { type = NavType.IntType })
    ) {
        val viewModel: RelationViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        RelationScreen(
            state = state,
            onOpenShow = { navController.navigate(ShowRoute(it)) },
            onErrorDismiss = viewModel::dismissError,
            onBack = navController::popBackStack
        )
    }
}

fun NavController.navigateToRelationScreen(showId: Int, popUpToTop: Boolean = false) {
    this.navigate(RELATION_SCREEN_PATTERN.replace("{showId}", showId.toString())) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}