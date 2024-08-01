package com.elfennani.aniwatch.presentation.screens.show

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.ImageLoader
import coil.compose.AsyncImage
import com.elfennani.aniwatch.formatSeconds
import com.elfennani.aniwatch.imageLoader
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.models.ShowSeason
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.Tag
import com.elfennani.aniwatch.presentation.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.presentation.composables.dummyShow
import com.elfennani.aniwatch.presentation.screens.episode.navigateToEpisodeScreen
import com.elfennani.aniwatch.presentation.screens.show.composables.EpisodeCard
import com.elfennani.aniwatch.presentation.screens.show.composables.ShowScreenHeader
import com.elfennani.aniwatch.presentation.screens.show.composables.ShowScreenSkeleton
import com.elfennani.aniwatch.presentation.screens.status.navigateToStatusEditorScreen
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun ShowScreen(
    state: ShowUiState,
    onBack: () -> Unit = {},
    onErrorDismiss: (Int) -> Unit = {},
    onOpenEpisode: (episode:Int) -> Unit = {},
    onStatusClick: () -> Unit = {},
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        snackbarHost = { ErrorSnackbarHost(
            errors = state.errors,
            onErrorDismiss = onErrorDismiss
        ) },
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
                    )
                }

                item{
                    Text(
                        text = "Episodes",
                        style = AppTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(horizontal = AppTheme.sizes.large)
                            .padding(vertical = AppTheme.sizes.medium)
                    )
                }

                items(state.show.episodes.sortedBy { it.episode }, key = {ep -> ep.id}) {
                    var subtitle: String? = null
                    if (it.dubbed || it.duration != null) {
                        val dubbed = if (it.dubbed) "Dubbed" else ""
                        val duration = if (it.duration != null) it.duration.formatSeconds() else ""
                        val connect = if (it.dubbed && it.duration != null) " • " else ""

                        subtitle = "$duration$connect$dubbed"
                    }

                    EpisodeCard(
                        title = it.name,
                        thumbnail = it.thumbnail,
                        subtitle = subtitle,
                        onClick = {
                            onOpenEpisode(it.episode)
                        }
                    )
                }
            }
        }
        if(state.isLoading){
            ShowScreenSkeleton(padding = padding)
        }
    }
}

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
            onStatusClick = {
                navController.navigateToStatusEditorScreen(showState.show?.id!!)
            }
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
