package com.elfennani.aniwatch.presentation.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun MainScaffold(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = { MainNavigation(navController) },
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        content = content
    )
}