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
import coil.compose.AsyncImage
import com.elfennani.aniwatch.formatSeconds
import com.elfennani.aniwatch.imageLoader
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.models.ShowSeason
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.Tag
import com.elfennani.aniwatch.presentation.screens.episode.navigateToEpisodeScreen
import com.elfennani.aniwatch.presentation.screens.show.composables.ShowScreenHeader
import com.elfennani.aniwatch.presentation.screens.show.composables.ShowScreenSkeleton
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun ShowScreen(
    state: ShowUiState,
    onBack: () -> Unit = {},
    onSnackBarDismiss: () -> Unit = {},
    onOpenEpisode: (episode:Int) -> Unit = {},
) {
    val lazyListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val imageLoader = LocalContext.current.imageLoader()

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
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { padding ->

        if (state.show != null && !state.isLoading) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
                item(key = "header") {
                    ShowScreenHeader(
                        show = state.show,
                        lazyListState = lazyListState,
                        padding = padding,
                        onBack = onBack,
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
                    Row(
                        modifier = Modifier
                            .clickable { onOpenEpisode(it.episode) }
                            .fillMaxWidth()
                            .padding(
                                horizontal = AppTheme.sizes.large,
                                vertical = AppTheme.sizes.normal
                            )
                            .height(IntrinsicSize.Min)
                    ){
                        AsyncImage(
                            model = it.thumbnail,
                            contentDescription = null,
                            imageLoader = imageLoader,
                            modifier = Modifier
                                .clip(AppTheme.shapes.thumbnail)
                                .background(AppTheme.colorScheme.secondary.copy(0.25f))
                                .width(128.dp)
                                .aspectRatio(16 / 9f)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(AppTheme.sizes.normal),
                            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller, Alignment.CenterVertically)
                        ) {
                            Text(text = "Episode ${it.episode}", style = AppTheme.typography.labelNormal)
                            if(it.dubbed || it.duration != null){
                                val dubbed = if(it.dubbed) "Dubbed" else ""
                                val duration = if(it.duration != null) it.duration.formatSeconds() else ""
                                val connect = if(it.dubbed && it.duration != null) " • " else ""

                                Text(text = "$duration$connect$dubbed", style = AppTheme.typography.labelSmall, color = AppTheme.colorScheme.onSecondary)
                            }
                        }
                    }
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
    val dummyShow = ShowDetails(
        id = 109731,
        allanimeId = "oso3xi8Knpv5BKqem",
        name = "Sound! Euphonium 3",
        description = "The third season of <i>Hibike! Euphonium</i>.<br><br>Kumiko's third year finally begins! With the concert band at Kitauji High School over 90 members, Kumiko is now the president and does her best with her final high school club activities to try to win her long-desired gold at nationals.<br><br>(Source: Crunchyroll, edited)",
        episodesCount = 13,
        episodes = listOf(
            Episode(
                id = "xvxSS5Mzdt9MtEh5R",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 3,
                name = "Ep 3",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/3_sub.jpg",
                duration = 1497
            ),
            Episode(
                id = "Ld577KNfZTXY8XT5u",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 2,
                name = "Ep 2",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/2_sub.jpg",
                duration = 1495
            ),
            Episode(
                id = "iCwfjXZJzDHr74Xw8",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 1,
                name = "Ep 1",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/1_sub.jpg",
                duration = 1497
            ),
            Episode(
                id = "G4J7aZMjrh2eZmSvi",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 5,
                name = "Ep 5",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/5_sub.jpg",
                duration = 1497
            ),
            Episode(
                id = "ARBy6J4cn8qezHGF3",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 4,
                name = "Ep 4",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/4_sub.jpg",
                duration = 1497
            ),
            Episode(
                id = "SJRLPXTEGM6e8gCsq",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 6,
                name = "Ep 6",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/6_sub.jpg",
                duration = 1497
            ),
            Episode(
                id = "hZ7m9P5nBgTuTgeXw",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 9,
                name = "Ep 9",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/9_sub.jpg",
                duration = 1497
            ),
            Episode(
                id = "mMpFXSniTN876SdaL",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 8,
                name = "Ep 8",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/8_sub.jpg",
                duration = 1497
            ),
            Episode(
                id = "mARMTfbc8JWDpBqrK",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 7,
                name = "Ep 7",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/7_sub.jpg",
                duration = 1497
            ),
            Episode(
                id = "4WtFCFWjpJLs9tGbm",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 12,
                name = "Ep 12",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/12_sub.jpg",
                duration = 1497
            ),
            Episode(
                id = "33MQMKfj29uni4mmA",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 13,
                name = "Ep 13",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/13_sub.jpg",
                duration = 1497
            )
        ),
        genres = listOf("Drama", "Music", "Slice of Life"),
        tags = listOf(
            Tag(id = 98, label = "Female Protagonist", percentage = 97, spoiler = false),
            Tag(id = 110, label = "Band", percentage = 94, spoiler = false),
            Tag(id = 84, label = "School Club", percentage = 94, spoiler = false),
            Tag(id = 102, label = "Coming of Age", percentage = 89, spoiler = false),
            Tag(id = 86, label = "Primarily Female Cast", percentage = 85, spoiler = false),
            Tag(id = 1673, label = "Classical Music", percentage = 85, spoiler = false),
            Tag(id = 1228, label = "Primarily Teen Cast", percentage = 83, spoiler = false),
            Tag(id = 46, label = "School", percentage = 65, spoiler = false),
            Tag(id = 294, label = "Bisexual", percentage = 60, spoiler = false),
            Tag(id = 153, label = "Time Skip", percentage = 53, spoiler = true)
        ),
        season = ShowSeason.SPRING,
        year = 2024,
        format = "TV",
        image = ShowImage(
            large = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx109731-PBjrX9CebGus.png",
            medium = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx109731-PBjrX9CebGus.png",
            original = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx109731-PBjrX9CebGus.png",
            color = Color(0xFFe4c95d)
        ),
        banner = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/109731-O9wpjWphEhKn.jpg",
        progress = 5,
        status = ShowStatus.WATCHING
    )
    AppTheme {
        ShowScreen(
            state = ShowUiState(
                show = dummyShow,
                isLoading = false,
                error = null
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
            onSnackBarDismiss = viewModel::dismissError,
            onOpenEpisode = {
                navController.navigateToEpisodeScreen(
                    id = showState.show?.id!!,
                    allanimeId = showState.show?.allanimeId!!,
                    episode = it,
                )
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
