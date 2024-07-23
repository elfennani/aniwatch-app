package com.elfennani.aniwatch.presentation.screens.listings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.elfennani.aniwatch.presentation.composables.MainScaffold
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun ListingScreen(
    navController: NavController
){
    MainScaffold(navController = navController) {
        Text(text = "This is listing Screen", style = AppTheme.typography.body)
    }
}

const val LISTING_SCREEN_PATTERN = "listing"
fun NavGraphBuilder.listingScreen(navController: NavController, padding: PaddingValues) {
    composable(LISTING_SCREEN_PATTERN) {
        ListingScreen(
            navController=navController,
        )
    }
}

fun NavController.navigateToListingScreen(popUpToTop: Boolean = false) {
    this.navigate(LISTING_SCREEN_PATTERN) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}
