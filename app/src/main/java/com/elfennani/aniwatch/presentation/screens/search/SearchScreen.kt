package com.elfennani.aniwatch.presentation.screens.search

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.presentation.screens.search.composables.SearchBox
import com.elfennani.aniwatch.presentation.composables.ShowCard
import com.elfennani.aniwatch.presentation.screens.show.navigateToShowScreen
import com.elfennani.aniwatch.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    query: String,
    onQueryChanged: (String) -> Unit,
    showsList: LazyPagingItems<ShowBasic>?,
) {
    Scaffold(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        topBar = { SearchBox(query, onQueryChanged) }
    ) {
        LazyColumn(
            contentPadding = it
        ) {
            if (showsList != null) {
                items(
                    count = showsList.itemCount,
                    key = showsList.itemKey { show -> show.id },
                ) { index ->
                    val item = showsList[index]
                    Log.d("SearchScreen", "SearchScreen: $item")
                    if (item != null) {
                        ShowCard(
                            show = item,
                            onClick = { navController.navigateToShowScreen(item.id) }
                        )
                    }
                }
                when (showsList.loadState.append) {
                    is LoadState.Error -> {
                        item {
                            val error =
                                (showsList.loadState.append as LoadState.Error).error.message
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(AppTheme.sizes.large)
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = AppTheme.colorScheme.error
                                )
                                Text(
                                    text = "Something went wrong",
                                    style = AppTheme.typography.titleNormal,
                                    color = AppTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                if (error != null) {
                                    Text(
                                        text = error,
                                        style = AppTheme.typography.body,
                                        color = AppTheme.colorScheme.error.copy(alpha = 0.75f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                TextButton(
                                    onClick = { showsList.retry() },
                                    colors = ButtonDefaults.textButtonColors()
                                        .copy(contentColor = AppTheme.colorScheme.primary)
                                ) {
                                    Text(text = "Retry again")
                                }
                            }
                        }
                    }

                    is LoadState.Loading -> {
                        item {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(AppTheme.sizes.large)
                                    .fillMaxWidth()
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    else -> {}
                }
                if (showsList.loadState.append.endOfPaginationReached) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(AppTheme.sizes.large)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "End Reached",
                                style = AppTheme.typography.body,
                                color = AppTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

const val SEARCH_SCREEN_PATTERN = "search"
fun NavGraphBuilder.searchScreen(navController: NavController) {
    composable(SEARCH_SCREEN_PATTERN) {
        val viewModel: SearchViewModel = hiltViewModel()
        val lazyPagingItemsState by viewModel.pagingDataFlow.collectAsState()
        val lazyPagingItems = lazyPagingItemsState?.collectAsLazyPagingItems()
        val query by viewModel.query.collectAsState()

        SearchScreen(
            navController = navController,
            query = query,
            onQueryChanged = viewModel::setQuery,
            showsList = lazyPagingItems,
        )
    }
}

fun NavController.navigateToSearchScreen(popUpToTop: Boolean = false) {
    this.navigate(SEARCH_SCREEN_PATTERN) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}