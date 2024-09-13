package com.elfennani.aniwatch.ui.screens.show

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastAny
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.ui.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.ui.composables.PillButton
import com.elfennani.aniwatch.ui.composables.dummyShow
import com.elfennani.aniwatch.ui.screens.characters.navigateToCharactersScreen
import com.elfennani.aniwatch.ui.screens.episode.EpisodeRoute
import com.elfennani.aniwatch.ui.screens.relations.navigateToRelationScreen
import com.elfennani.aniwatch.ui.screens.show.composables.EpisodeCard
import com.elfennani.aniwatch.ui.screens.show.composables.EpisodeDialog
import com.elfennani.aniwatch.ui.screens.show.composables.ShowScreenHeader
import com.elfennani.aniwatch.ui.screens.show.composables.ShowScreenSkeleton
import com.elfennani.aniwatch.ui.screens.show.composables.TagsList
import com.elfennani.aniwatch.ui.screens.status.navigateToStatusEditorScreen
import com.elfennani.aniwatch.ui.theme.AppTheme
import kotlinx.serialization.Serializable

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ShowScreen(
    state: ShowUiState,
    onBack: () -> Unit = {},
    onErrorDismiss: (Int) -> Unit = {},
    onOpenEpisode: (episode: Int, audio: EpisodeAudio) -> Unit = { _, _ -> },
    onDownloadEpisode: (episode: Int, audio: EpisodeAudio) -> Unit = { _, _ -> },
    onDeleteEpisode: (episode: Int) -> Unit = {},
    onStatusClick: () -> Unit = {},
    onClickCharacters: (Int) -> Unit = {},
    onClickRelations: (Int) -> Unit = {},
    onToggleAudio: () -> Unit = {},
) {
    val lazyListState = rememberLazyListState()
    var selectedEpisode by remember {
        mutableStateOf<String?>(null)
    }
    var tagsOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(true)

    if (tagsOpen && state.show != null) {
        ModalBottomSheet(
            onDismissRequest = { tagsOpen = false },
            containerColor = AppTheme.colorScheme.background,
            contentColor = AppTheme.colorScheme.onBackground,
            sheetState = sheetState
        ) {
            TagsList(state.show.tags)
        }
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
                            PillButton(
                                onClick = { tagsOpen = true },
                                text = "Tags",
                                icon = Icons.Default.Tag
                            )
                        }

//                        TagsList(state.show.tags, tagsOpen)
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = AppTheme.sizes.large),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Episodes",
                            style = AppTheme.typography.labelLarge,
                            modifier = Modifier

                                .padding(vertical = AppTheme.sizes.medium)
                        )
                        if (state.defaultAudio != null && state.show.episodes.fastAny { it.dubbed })
                            AnimatedContent(
                                targetState = state.defaultAudio,
                                label = ""
                            ) { defaultAudio ->
                                PillButton(
                                    onClick = { onToggleAudio() },
                                    text = defaultAudio.name,
                                    icon = Icons.Default.Language
                                )
                            }
                    }
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
                        onClick = {
                            val audio = when {
                                episode.dubbed -> state.defaultAudio ?: EpisodeAudio.SUB
                                else -> EpisodeAudio.SUB
                            }
                            onOpenEpisode(episode.episode, audio)
                        },
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
                onOpenEpisode = onOpenEpisode,
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

@Serializable
data class ShowRoute(val id: Int)

fun NavGraphBuilder.showScreen(navController: NavController) {
    composable<ShowRoute> {
        val viewModel: ShowViewModel = hiltViewModel()
        val showState by viewModel.state.collectAsStateWithLifecycle()

        ShowScreen(
            state = showState,
            onBack = navController::popBackStack,
            onErrorDismiss = viewModel::dismissError,
            onOpenEpisode = { episode, audio ->
                val state = showState
                    .show?.episodes?.find { it.episode == episode }
                    ?.state

                navController.navigate(
                    EpisodeRoute(
                        id = showState.show?.id!!,
                        allanimeId = showState.show?.allanimeId!!,
                        episode = episode,
                        audio = if (state is DownloadState.Downloaded) state.audio else audio,
                        useSaved = state is DownloadState.Downloaded
                    )
                )
            },
            onDownloadEpisode = viewModel::downloadEpisode,
            onDeleteEpisode = viewModel::deleteEpisode,
            onStatusClick = {
                navController.navigateToStatusEditorScreen(showState.show?.id!!)
            },
            onClickCharacters = navController::navigateToCharactersScreen,
            onClickRelations = navController::navigateToRelationScreen,
            onToggleAudio = viewModel::toggleAudio
        )
    }
}