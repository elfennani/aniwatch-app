package com.elfennani.aniwatch.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.MediaType
import com.elfennani.aniwatch.plus
import com.elfennani.aniwatch.presentation.composables.SearchBoxButton
import com.elfennani.aniwatch.presentation.composables.Section
import com.elfennani.aniwatch.presentation.composables.items
import com.elfennani.aniwatch.presentation.screens.home.composables.ActivityCard
import com.elfennani.aniwatch.presentation.screens.home.composables.WatchingCardSkeleton
import com.elfennani.aniwatch.presentation.screens.home.composables.WatchingShowsSection
import com.elfennani.aniwatch.presentation.screens.show.navigateToShowScreen
import com.elfennani.aniwatch.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    state: HomeUiState,
    onRefetch: () -> Unit,
    onDismissError: () -> Unit,
    rootPadding: PaddingValues,
    feed: LazyPagingItems<Activity>,
) {
    val lazyListState = rememberLazyListState()
    val pullState = rememberPullToRefreshState(positionalThreshold = 100.dp)
    val snackbarHostState = remember { SnackbarHostState() }
    val dir = LocalLayoutDirection.current
    val isDarkMode = isSystemInDarkTheme()
    val logoResource =
        if (isDarkMode) R.drawable.aniwatch_dark_mode else R.drawable.aniwatch_light_mode

    LaunchedEffect(pullState.isRefreshing) {
        if (pullState.isRefreshing) {
            onRefetch()
            feed.refresh()
        }
    }

    LaunchedEffect(key1 = state.isFetching) {
        if (!state.isFetching) pullState.endRefresh()
    }

    if (state.error != null) {
        LaunchedEffect(key1 = state.error) {
            val result = snackbarHostState.showSnackbar(state.error)
            if (result == SnackbarResult.Dismissed) {
                onDismissError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.add(
            WindowInsets(
                left = rootPadding.calculateLeftPadding(dir),
                right = rootPadding.calculateRightPadding(dir),
                top = 0.dp,
                bottom = rootPadding.calculateBottomPadding()
            )
        )
    ) { containerPadding ->
        Box(Modifier.nestedScroll(pullState.nestedScrollConnection)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
                state = lazyListState,
                contentPadding = PaddingValues(vertical = AppTheme.sizes.medium) + PaddingValues(
                    top = containerPadding.calculateTopPadding(),
                    bottom = containerPadding.calculateBottomPadding()
                )
            ) {
                item {
                    Column(
                        Modifier
                            .padding(AppTheme.sizes.medium)
                            .padding(bottom = AppTheme.sizes.small),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
                    ) {
                        Row {
                            Image(
                                painter = painterResource(id = logoResource),
                                contentDescription = null,
                                modifier = Modifier.height(AppTheme.sizes.medium)
                            )
                        }
                        SearchBoxButton()
                    }
                }
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
                    ) {
                        WatchingShowsSection(
                            shows = state.shows,
                            onPressShow = { navController.navigateToShowScreen(it) },
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
                                navController.navigateToShowScreen(item.show.id)
                            }
                        })
                        Divider(color = AppTheme.colorScheme.onBackground.copy(alpha = 0.1f))
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

const val HomeScreenPattern = "auth/home"
fun NavGraphBuilder.homeScreen(navController: NavController, padding: PaddingValues) {
    composable(route = HomeScreenPattern) {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeState by viewModel.state.collectAsState()
        val feed = viewModel.feedPagingFlow.collectAsLazyPagingItems()

        HomeScreen(
            navController = navController,
            state = homeState,
            feed = feed,
            onRefetch = viewModel::refetch,
            onDismissError = viewModel::hideError,
            rootPadding = padding
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