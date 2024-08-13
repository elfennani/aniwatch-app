package com.elfennani.aniwatch.presentation.screens.downloads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.PlayForWork
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.elfennani.aniwatch.models.Download
import com.elfennani.aniwatch.presentation.composables.MainScaffold
import com.elfennani.aniwatch.presentation.screens.settings.SettingsScreen
import com.elfennani.aniwatch.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(
    navController: NavController,
    downloads: List<Download>,
    onStartWork: () -> Unit = {},
    onClearAll: () -> Unit = {}
) {
    Scaffold(
        backgroundColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Downloads")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.card,
                    titleContentColor = AppTheme.colorScheme.onCard,
                    navigationIconContentColor = AppTheme.colorScheme.onCard,
                    actionIconContentColor = AppTheme.colorScheme.onCard
                ),
                actions = {
                    IconButton(onClick = { onClearAll() }) {
                        Icon(imageVector = Icons.Default.ClearAll, contentDescription = null)
                    }
                    IconButton(onClick = { onStartWork() }) {
                        Icon(imageVector = Icons.Default.PlayForWork, contentDescription = null)
                    }
                }
            )
        }
    ) {
        LazyColumn(
            contentPadding = it
        ) {
            items(downloads){ download ->
                Column {
                    Text(text = download.title)
                    Text(text = "${download.episode.toString()} • ${download.status}")
                }
            }
        }
    }
}

const val DOWNLOADS_SCREEN_PATTERN = "settings"
fun NavGraphBuilder.downloadsScreen(navController: NavController, padding: PaddingValues) {
    composable(DOWNLOADS_SCREEN_PATTERN) {
        val viewModel: DownloadsViewModel = hiltViewModel()
        val downloads by viewModel.downloads.collectAsStateWithLifecycle()

        DownloadsScreen(
            navController = navController,
            downloads = downloads,
            onStartWork = viewModel::forceStartDownload,
            onClearAll = viewModel::clearDownloads
        )
    }
}

fun NavController.navigateToDownloadsScreen(popUpToTop: Boolean = false) {
    this.navigate(DOWNLOADS_SCREEN_PATTERN) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}