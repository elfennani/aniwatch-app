package com.elfennani.aniwatch.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.elfennani.aniwatch.models.MediaType
import com.elfennani.aniwatch.plus
import com.elfennani.aniwatch.presentation.composables.SearchBoxButton
import com.elfennani.aniwatch.presentation.composables.Section
import com.elfennani.aniwatch.presentation.screens.home.composables.ActivityCard
import com.elfennani.aniwatch.presentation.screens.home.composables.Header
import com.elfennani.aniwatch.presentation.screens.home.composables.WatchingShowsSection
import com.elfennani.aniwatch.presentation.screens.show.navigateToShowScreen
import com.elfennani.aniwatch.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    state: HomeUiState,
    onRefetch: () -> Unit,
    onDismissError: () -> Unit,
    rootPadding: PaddingValues,
) {
    val lazyListState = rememberLazyListState()
    val pullState = rememberPullToRefreshState(positionalThreshold = 100.dp)
    val snackbarHostState = remember { SnackbarHostState() }
    val dir = LocalLayoutDirection.current

    LaunchedEffect(pullState.isRefreshing) {
        if (pullState.isRefreshing) {
            onRefetch()
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
                    Box(Modifier.padding(horizontal = AppTheme.sizes.medium)) {
                        SearchBoxButton()
                    }
                }
                item { Spacer(modifier = Modifier.height(AppTheme.sizes.medium)) }
                item {
                    WatchingShowsSection(
                        shows = state.shows,
                        onPressShow = { navController.navigateToShowScreen(it) }
                    )
                }
                item { Spacer(modifier = Modifier.height(AppTheme.sizes.medium)) }
                item {
                    Section(title = "My Feed", modifier = Modifier.padding(horizontal = AppTheme.sizes.medium))
                }
                item { Spacer(modifier = Modifier.height(AppTheme.sizes.medium)) }
                items(state.feed) {
                    ActivityCard(activity = it, onClick = {
                        if(it.show != null && it.show.type == MediaType.ANIME){
                            navController.navigateToShowScreen(it.show.id)
                        }
                    })
                    Divider(color = AppTheme.colorScheme.onBackground.copy(alpha = 0.1f))
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

        HomeScreen(
            navController = navController,
            state = homeState,
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