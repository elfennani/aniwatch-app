package com.elfennani.aniwatch.presentation.screens.show

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.presentation.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.presentation.composables.PillButton
import com.elfennani.aniwatch.presentation.composables.dummyShow
import com.elfennani.aniwatch.presentation.screens.characters.navigateToCharactersScreen
import com.elfennani.aniwatch.presentation.screens.episode.navigateToEpisodeScreen
import com.elfennani.aniwatch.presentation.screens.relations.navigateToRelationScreen
import com.elfennani.aniwatch.presentation.screens.show.composables.EpisodeCard
import com.elfennani.aniwatch.presentation.screens.show.composables.EpisodeDialog
import com.elfennani.aniwatch.presentation.screens.show.composables.ShowScreenHeader
import com.elfennani.aniwatch.presentation.screens.show.composables.ShowScreenSkeleton
import com.elfennani.aniwatch.presentation.screens.status.navigateToStatusEditorScreen
import com.elfennani.aniwatch.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ShowScreen(
    state: ShowUiState,
    onBack: () -> Unit = {},
    onErrorDismiss: (Int) -> Unit = {},
    onOpenEpisode: (episode: Int) -> Unit = {},
    onDownloadEpisode: (episode: Int, audio: EpisodeAudio) -> Unit = { _, _ -> },
    onDeleteEpisode: (episode: Int) -> Unit = {},
    onStatusClick: () -> Unit = {},
    onClickCharacters: (Int) -> Unit = {},
    onClickRelations: (Int) -> Unit = {},
) {
    val lazyListState = rememberLazyListState()
    var selectedEpisode by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        snackbarHost = {
            ErrorSnackbarHost(
                errors = state.errors,
                onErrorDismiss = onErrorDismiss
            )
        },
    ) { padding ->
        if (state.show != null && !state.isLoading) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
                item(key = "header") {
                    ShowScreenHeader(
                        show = state.show,
                        lazyListState = lazyListState,
                        padding = padding,
                        onBack = onBack,
                        onStatusClick = onStatusClick
                    ) {
                        FlowRow(
                            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.small),
                            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)
                        ) {
                            PillButton(
                                onClick = { onClickCharacters(state.show.id) },
                                text = "Characters",
                                icon = Icons.Default.Groups
                            )
                            PillButton(
                                onClick = { onClickRelations(state.show.id) },
                                text = "Relations",
                                icon = Icons.Default.AccountTree
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Episodes",
                        style = AppTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(horizontal = AppTheme.sizes.large)
                            .padding(vertical = AppTheme.sizes.medium)
                    )
                }

                items(
                    state.show.episodes.sortedBy { it.episode },
                    key = { ep -> ep.id }
                ) { episode ->
                    EpisodeCard(
                        modifier = Modifier.apply {
                            if (
                                state.show.progress != null &&
                                episode.episode <= state.show.progress &&
                                state.show.status.isWatching()
                            ) {
                                alpha(0.75f)
                            }
                        },
                        episode = episode,
                        onClick = { onOpenEpisode(episode.episode) },
                        onOptions = { selectedEpisode = episode.id }
                    )
                }
            }
        }
        if (state.isLoading) {
            ShowScreenSkeleton(padding = padding)
        }
        if (selectedEpisode != null) {
            val episode = state.show?.episodes?.find { it.id == selectedEpisode }!!
            EpisodeDialog(
                onDismissRequest = { selectedEpisode = null },
                episode = episode,
                onDownload = { onDownloadEpisode(episode.episode, it) },
                onDelete = { onDeleteEpisode(episode.episode) }
            )
        }
    }
}

private fun ShowStatus?.isWatching() = this == ShowStatus.WATCHING || this == ShowStatus.REPEATING

@Preview
@Composable
private fun ShowScreenPreview() {
    AppTheme {
        ShowScreen(
            state = ShowUiState(
                show = dummyShow,
                isLoading = false,
                errors = emptyList()
            )
        )
    }
}

const val SHOW_SCREEN_PATTERN = "show/{id}"
fun NavGraphBuilder.showScreen(navController: NavController) {
    composable(
        route = SHOW_SCREEN_PATTERN,
        arguments = listOf(navArgument("id") { type = NavType.IntType }),
    ) {
        val viewModel: ShowViewModel = hiltViewModel()
        val showState by viewModel.state.collectAsState()

        ShowScreen(
            state = showState,
            onBack = navController::popBackStack,
            onErrorDismiss = viewModel::dismissError,
            onOpenEpisode = {
                navController.navigateToEpisodeScreen(
                    id = showState.show?.id!!,
                    allanimeId = showState.show?.allanimeId!!,
                    episode = it,
                )
            },
            onDownloadEpisode = viewModel::downloadEpisode,
            onDeleteEpisode = viewModel::deleteEpisode,
            onStatusClick = {
                navController.navigateToStatusEditorScreen(showState.show?.id!!)
            },
            onClickCharacters = navController::navigateToCharactersScreen,
            onClickRelations = navController::navigateToRelationScreen
        )
    }
}

fun NavController.navigateToShowScreen(id: Int, popUpToTop: Boolean = false) {
    this.navigate(SHOW_SCREEN_PATTERN.replace("{id}", id.toString())) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}
