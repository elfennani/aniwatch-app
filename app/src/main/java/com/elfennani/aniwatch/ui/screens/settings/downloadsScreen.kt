package com.elfennani.aniwatch.ui.screens.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.elfennani.aniwatch.ui.composables.MainScaffold

@Composable
fun SettingsScreen(
    navController: NavController
){
    MainScaffold(navController = navController) {
        Text(text = "This is settings Screen", fontSize = 16.sp)
    }
}

const val SETTINGS_SCREEN_PATTERN = "settings"
fun NavGraphBuilder.settingsScreen(navController: NavController) {
    composable(SETTINGS_SCREEN_PATTERN) {
        SettingsScreen(
            navController=navController,
        )
    }
}

fun NavController.navigateToSettingsScreen(popUpToTop: Boolean = false) {
    this.navigate(SETTINGS_SCREEN_PATTERN) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}