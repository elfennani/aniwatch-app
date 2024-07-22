package com.elfennani.aniwatch.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.elfennani.aniwatch.domain.models.ShowBasic

@Composable
fun HomeScreen(
    shows: List<ShowBasic>,
    error: String? = null
) {
    val state = rememberLazyListState()

    Scaffold {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            state = state
        ) {
            item { Text(text = "This is the home screen") }
            if (error != null) {
                item {
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }
            }
            items(shows) { show ->
                Text(show.name)
            }
        }
    }
}

const val HomeScreenPattern = "auth/home"
fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(HomeScreenPattern) {
        val viewModel: HomeViewModel = hiltViewModel();
        val shows = viewModel.shows.collectAsState()
        val error by viewModel.error.collectAsState()

        HomeScreen(
            shows = shows.value,
            error = error,
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