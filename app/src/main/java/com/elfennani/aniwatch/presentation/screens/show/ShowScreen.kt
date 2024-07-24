package com.elfennani.aniwatch.presentation.screens.show

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.elfennani.aniwatch.formatSeconds
import com.elfennani.aniwatch.presentation.screens.episode.navigateToEpisodeScreen
import com.elfennani.aniwatch.presentation.theme.AppTheme
import java.util.Locale

@Composable
fun ShowScreen(
    navController: NavController,
    state: ShowUiState,
    onSnackBarDismiss: () -> Unit = {}
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    if (state.error != null) {
        LaunchedEffect(Unit) {
            val result = snackbarHostState.showSnackbar(
                state.error,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            if (result == SnackbarResult.Dismissed)
                onSnackBarDismiss()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = AppTheme.sizes.large),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
        ) {
            item { Box(modifier = Modifier.height(it.calculateTopPadding() + AppTheme.sizes.large)) }
            item {
                if (state.show != null) {
                    val show = state.show
                    val season = show.season.name
                        .mapIndexed { index, c ->
                            if (index == 0) c.uppercaseChar()
                            else c.lowercase()
                        }.joinToString("")

                    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)) {
                        AsyncImage(
                            model = show.image.original,
                            contentDescription = show.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(128.dp)
                                .aspectRatio(0.69f)
                                .clip(AppTheme.shapes.card)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)) {
                            Text(text = show.name, style = AppTheme.typography.titleNormal)
                            Text(
                                text = "$season • ${show.year}",
                                style = AppTheme.typography.labelSmall,
                                color = AppTheme.colorScheme.onSecondary
                            )
                        }
                        Divider(color = AppTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                    }
                }
            }
            item {
                Text(
                    text = "Episodes",
                    style = AppTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = AppTheme.sizes.large)
                )
            }
            val eps = state.show?.episodes?.sortedBy {
                it.episode
            }
            items(eps ?: emptyList()) { episode ->
                Row(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .clip(AppTheme.shapes.button)
                        .clickable { navController.navigateToEpisodeScreen(state.show?.id!!,state.show.allanimeId, episode.episode) },
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = episode.thumbnail,
                        contentDescription = null,
                        modifier = Modifier
                            .height(64.dp)
                            .aspectRatio(16 / 9f)
                            .clip(AppTheme.shapes.button)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            AppTheme.sizes.smaller,
                            Alignment.CenterVertically
                        ),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Episode ${episode.episode}",
                            style = AppTheme.typography.body,
                        )
                        val duration = if(episode.duration != null) episode.duration.formatSeconds() else ""
                        val dubbed = if (episode.dubbed) "Dubbed" else ""
                        val connector = if (duration.isNotEmpty() && dubbed.isNotEmpty()) " • " else ""

                        Text(
                            text = "$duration$connector$dubbed",
                            style = AppTheme.typography.labelSmall,
                            color = AppTheme.colorScheme.onSecondary
                        )
                    }
                }
            }

            item { Box(modifier = Modifier.height(it.calculateTopPadding() + AppTheme.sizes.large)) }
        }
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
            navController = navController,
            state = showState,
            onSnackBarDismiss = viewModel::dismissError
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
