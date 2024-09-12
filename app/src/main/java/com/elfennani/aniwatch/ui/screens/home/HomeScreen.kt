package com.elfennani.aniwatch.ui.screens.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.MediaType
import com.elfennani.aniwatch.utils.plus
import com.elfennani.aniwatch.ui.composables.ErrorSnackbarHost
import com.elfennani.aniwatch.ui.composables.Section
import com.elfennani.aniwatch.ui.screens.home.composables.ActivityCard
import com.elfennani.aniwatch.ui.screens.home.composables.HomeHeader
import com.elfennani.aniwatch.ui.screens.home.composables.WatchingShowsSection
import com.elfennani.aniwatch.ui.screens.search.navigateToSearchScreen
import com.elfennani.aniwatch.ui.screens.show.ShowRoute
import com.elfennani.aniwatch.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    state: HomeUiState,
    onRefetch: () -> Unit,
    onErrorDismiss: (Int) -> Unit,
    feed: LazyPagingItems<Activity>,
) {
    val lazyListState = rememberLazyListState()
    val pullState = rememberPullToRefreshState(positionalThreshold = 100.dp)
    val snackbarHostState = remember { SnackbarHostState() }
    val dir = LocalLayoutDirection.current

    LaunchedEffect(pullState.isRefreshing) {
        if (pullState.isRefreshing) {
            onRefetch()
            feed.refresh()
        }
    }

    LaunchedEffect(key1 = state.isFetching) {
        if (!state.isFetching) pullState.endRefresh()
    }

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
            Box(Modifier.nestedScroll(pullState.nestedScrollConnection)) {
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

                if (pullState.progress > 0 || pullState.isRefreshing) {
                    val extraTopPadding = if (pullState.isRefreshing) 20.dp else 0.dp
                    PullToRefreshContainer(
                        modifier = Modifier
                            .padding(top = containerPadding.calculateTopPadding() + extraTopPadding)
                            .align(Alignment.TopCenter)
                            .absoluteOffset(y = (-50).dp),
                        state = pullState,
                    )
                }
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
        val feed = viewModel.feedPagingFlow.collectAsLazyPagingItems()

        HomeScreen(
            navController = navController,
            state = homeState,
            feed = feed,
            onRefetch = viewModel::refetch,
            onErrorDismiss = viewModel::dismissError,
        )
    }
}

fun NavController.navigateToHomeScreen(popUpToTop: Boolean = false) {
    this.navigate(HomeScreenPattern) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}