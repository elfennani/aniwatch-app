package com.elfennani.aniwatch.ui.screens.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VoiceOverOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.domain.models.Character
import com.elfennani.aniwatch.ui.screens.characters.composables.CharacterGridCard
import com.elfennani.aniwatch.ui.screens.characters.composables.CharacterListCard
import com.elfennani.aniwatch.ui.theme.AppTheme
import com.elfennani.aniwatch.utils.plus
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    characters: LazyPagingItems<com.elfennani.aniwatch.domain.models.Character>,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val showVAKey = booleanPreferencesKey("SHOW_VA")
    val displayModeKey = booleanPreferencesKey("DISPLAY_MODE")

    val showVA by context.dataStore.data
        .map { it[showVAKey] ?: true }
        .collectAsState(initial = true)

    val listDisplayMode by context.dataStore.data
        .map { it[displayModeKey] ?: false }
        .collectAsState(initial = false)

    Scaffold(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = AppTheme.colorScheme.card,
                    titleContentColor = AppTheme.colorScheme.onCard,
                    navigationIconContentColor = AppTheme.colorScheme.onCard,
                    actionIconContentColor = AppTheme.colorScheme.onCard
                ),
                title = {
                    Text(text = "Characters", style = AppTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch { context.dataStore.edit { it[displayModeKey] = !listDisplayMode } }
                    }) {
                        Icon(
                            imageVector = if (listDisplayMode) Icons.Default.GridView else Icons.AutoMirrored.Default.List,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        scope.launch { context.dataStore.edit { it[showVAKey] = !showVA } }
                    }) {
                        Icon(
                            imageVector = if (showVA) Icons.Default.VoiceOverOff else Icons.Default.RecordVoiceOver,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (!listDisplayMode) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
                contentPadding = padding.plus(PaddingValues(AppTheme.sizes.medium))
            ) {
                items(
                    count = characters.itemCount,
                    key = characters.itemKey { it.id }
                ) {
                    val character = characters[it]

                    if (character != null)
                        CharacterGridCard(character = character,showVA = showVA)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
                contentPadding = padding.plus(PaddingValues(AppTheme.sizes.medium))
            ) {
                items(
                    count = characters.itemCount,
                    key = characters.itemKey { it.id }
                ) {
                    val character = characters[it]

                    if (character != null)
                        CharacterListCard(character = character, showVA = showVA)
                }
            }
        }
    }
}

const val CHARACTERS_SCREEN_PATTERN = "characters/{showId}"
fun NavGraphBuilder.charactersScreen(navController: NavController) {
    composable(
        route = CHARACTERS_SCREEN_PATTERN,
        arguments = listOf(
            navArgument("showId") { type = NavType.IntType }
        )
    ) {
        val viewModel: CharactersViewModel = hiltViewModel()
        val characters = viewModel.charactersFlow.collectAsLazyPagingItems()

        CharactersScreen(
            characters = characters,
            onBack = navController::popBackStack
        )
    }
}

fun NavController.navigateToCharactersScreen(showId: Int, popUpToTop: Boolean = false) {
    this.navigate(CHARACTERS_SCREEN_PATTERN.replace("{showId}", showId.toString())) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}

@Preview
@Composable
private fun CharactersScreenPrev() {
    val pagingData = flowOf(PagingData.empty<com.elfennani.aniwatch.domain.models.Character>()).collectAsLazyPagingItems()
    AppTheme {
        CharactersScreen(characters = pagingData, onBack = {})
    }
}