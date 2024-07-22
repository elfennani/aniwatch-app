package com.elfennani.aniwatch.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.presentation.composables.WatchingCard
import com.elfennani.aniwatch.presentation.screens.home.composables.Header
import com.elfennani.aniwatch.presentation.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    shows: List<ShowBasic>,
    error: String? = null,
    isRefetching: Boolean,
    onRefetch: () -> Unit
) {
    val state = rememberLazyListState()
    val pullState = rememberPullToRefreshState(positionalThreshold = 100.dp)

    if (pullState.isRefreshing) {
        LaunchedEffect(true) {
            onRefetch()
        }
    }

    LaunchedEffect(key1 = isRefetching) {
        if (!isRefetching) pullState.endRefresh()
    }

    Scaffold(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground
    ) {
        Box(
            Modifier
                .nestedScroll(pullState.nestedScrollConnection)
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppTheme.sizes.normal),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                state = state,
            ) {
                item {
                    Header()
                }
                if (error != null) {
                    item {
                        Text(text = error, color = MaterialTheme.colorScheme.error)
                    }
                }
                items(shows) { show ->
                    WatchingCard(show = show)
                }
            }

            if (pullState.progress > 0 || pullState.isRefreshing) {
                PullToRefreshContainer(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .absoluteOffset(y = (-50).dp),
                    state = pullState,
                )
            }
        }
    }
}

const val HomeScreenPattern = "auth/home"
fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(HomeScreenPattern) {
        val viewModel: HomeViewModel = hiltViewModel()
        val shows = viewModel.shows.collectAsState()
        val error by viewModel.error.collectAsState()
        val isFetching = viewModel.isFetching.collectAsState()

        HomeScreen(
            shows = shows.value,
            error = error,
            isRefetching = isFetching.value,
            onRefetch = viewModel::refetch
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