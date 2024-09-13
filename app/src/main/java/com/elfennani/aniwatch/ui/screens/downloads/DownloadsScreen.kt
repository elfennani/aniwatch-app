package com.elfennani.aniwatch.ui.screens.downloads

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.ui.screens.episode.EpisodeRoute
import com.elfennani.aniwatch.ui.screens.show.ShowRoute
import com.elfennani.aniwatch.ui.theme.AppTheme
import com.elfennani.aniwatch.utils.imageLoader
import com.elfennani.aniwatch.utils.plus

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DownloadsScreen(
    downloads: Map<String, List<Pair<ShowDetails, Episode>>>,
    onNavigate: (Any) -> Unit,
) {
    val imageLoader = LocalContext.current.imageLoader()

    Scaffold(
        backgroundColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Downloads",
                        style = AppTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.card,
                    titleContentColor = AppTheme.colorScheme.onCard,
                    navigationIconContentColor = AppTheme.colorScheme.onCard,
                    actionIconContentColor = AppTheme.colorScheme.onCard
                ),
            )
        }
    ) {
        LazyColumn(
            contentPadding = it
                    + PaddingValues(horizontal = AppTheme.sizes.medium)
                    + PaddingValues(bottom = 64.dp),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal),
            modifier = Modifier.fillMaxWidth()
        ) {
            downloads.forEach { (state, episodes) ->
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.colorScheme.background)
                            .padding(top = AppTheme.sizes.medium, bottom = AppTheme.sizes.small)

                    ) {
                        Text(
                            text = state,
                            style = AppTheme.typography.titleNormal,
                            modifier = Modifier.padding(horizontal = AppTheme.sizes.small)
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .offset(y = AppTheme.sizes.small)
                        )
                    }
                }

                items(episodes) { (show, episode) ->
                    Row(
                        modifier = Modifier
                            .clip(AppTheme.shapes.card)
                            .combinedClickable(
                                onClick = {
                                    if (episode.state is DownloadState.Downloaded)
                                        onNavigate(
                                            EpisodeRoute(
                                                id = show.id,
                                                allanimeId = show.allanimeId,
                                                episode = episode.episode.toFloat(),
                                                audio = episode.state.audio,
                                                useSaved = true
                                            )
                                        )
                                    else {
                                        onNavigate(ShowRoute(id = show.id))
                                    }
                                },
                                onLongClick = { onNavigate(ShowRoute(id = show.id)) }
                            )
                            .background(AppTheme.colorScheme.card, AppTheme.shapes.card)
                            .padding(AppTheme.sizes.normal)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal)
                    ) {
                        AsyncImage(
                            model = episode.thumbnail,
                            contentDescription = null,
                            imageLoader = imageLoader,
                            modifier = Modifier
                                .clip(AppTheme.shapes.thumbnail)
                                .background(AppTheme.colorScheme.secondary.copy(0.25f))
                                .height(64.dp)
                                .align(Alignment.Top)
                                .aspectRatio(16 / 9f)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller),
                        ) {
                            Text(text = show.name, style = AppTheme.typography.labelLarge)
                            Text(
                                text = "Episode ${episode.episode}",
                                style = AppTheme.typography.labelSmall
                            )
                            if (episode.state is DownloadState.Downloading) {
                                LinearProgressIndicator(
                                    progress = episode.state.progress,
                                    color = AppTheme.colorScheme.primary,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

const val DOWNLOADS_SCREEN_PATTERN = "settings"
fun NavGraphBuilder.downloadsScreen(navController: NavController) {
    composable(
        DOWNLOADS_SCREEN_PATTERN,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        val viewModel: DownloadsViewModel = hiltViewModel()
        val downloads by viewModel.downloads.collectAsStateWithLifecycle()

        DownloadsScreen(
            downloads = downloads,
            onNavigate = navController::navigate
        )
    }
}