package com.elfennani.aniwatch.ui.screens.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.elfennani.aniwatch.domain.models.Activity
import com.elfennani.aniwatch.domain.models.enums.MediaType
import com.elfennani.aniwatch.ui.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.ui.composables.Section
import com.elfennani.aniwatch.ui.screens.home.composables.ActivityCard
import com.elfennani.aniwatch.ui.screens.home.composables.HomeHeader
import com.elfennani.aniwatch.ui.screens.home.composables.WatchingShowsSection
import com.elfennani.aniwatch.ui.screens.search.navigateToSearchScreen
import com.elfennani.aniwatch.ui.screens.show.ShowRoute
import com.elfennani.aniwatch.ui.theme.AppTheme
import com.elfennani.aniwatch.utils.plus

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    state: HomeUiState,
    onRefetch: () -> Unit,
    onErrorDismiss: (Int) -> Unit,
    feed: LazyPagingItems<Activity>,
) {
    val lazyListState = rememberLazyListState()
    val isRefreshing by remember {
        derivedStateOf {
            state.isFetching || feed.loadState.refresh is LoadState.Loading
        }
    }
    val pullState = rememberPullRefreshState(isRefreshing, onRefetch)
    val snackbarHostState = remember { SnackbarHostState() }

    if (state.errors.isNotEmpty()) {
        val errorMessage = stringResource(id = state.errors.first())
        LaunchedEffect(errorMessage, snackbarHostState) {
            snackbarHostState.showSnackbar(message = errorMessage)
            onErrorDismiss(state.errors.first())
        }
    }

    Scaffold(
        snackbarHost = { ErrorSnackbarHost(errors = state.errors, onErrorDismiss) },
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
    ) { containerPadding ->
        val horizontal = PaddingValues(bottom = containerPadding.calculateBottomPadding())
        val vertical = PaddingValues(vertical = AppTheme.sizes.medium)
        val contentPadding = horizontal + vertical

        Column(
            modifier = Modifier.padding(top = containerPadding.calculateTopPadding())
        ) {
            HomeHeader(
                border = lazyListState.canScrollBackward,
                user = state.user,
                onSearch = navController::navigateToSearchScreen
            )
            Box(Modifier.pullRefresh(pullState)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    contentPadding = contentPadding
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
                        ) {
                            WatchingShowsSection(
                                shows = state.shows,
                                onPressShow = { navController.navigate(ShowRoute(id = it)) },
                                isLoading = state.isLoading
                            )

                            Section(
                                title = "My Feed",
                                modifier = Modifier.padding(horizontal = AppTheme.sizes.medium)
                            )

                            Spacer(modifier = Modifier)
                        }
                    }

                    items(
                        count = feed.itemCount,
                        key = feed.itemKey { activity -> activity.id }
                    ) { index ->
                        val item = feed[index]
                        if (item != null) {
                            ActivityCard(activity = item, onClick = {
                                if (item.show != null && item.show.type == MediaType.ANIME) {
                                    navController.navigate(ShowRoute(item.show.id))
                                }
                            })
                            HorizontalDivider(color = AppTheme.colorScheme.onBackground.copy(alpha = 0.07f))
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}

const val HomeScreenPattern = "auth/home"
fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(
        route = HomeScreenPattern,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeState by viewModel.state.collectAsState()
        val feed = viewModel.lazyFeed.collectAsLazyPagingItems()

        HomeScreen(
            navController = navController,
            state = homeState,
            feed = feed,
            onRefetch = {
                feed.refresh()
                viewModel.onRefresh()
            },
            onErrorDismiss = viewModel::dismissError,
        )
    }
}