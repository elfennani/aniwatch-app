package com.elfennani.aniwatch.presentation.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.elfennani.aniwatch.presentation.screens.home.HomeScreenPattern
import com.elfennani.aniwatch.presentation.screens.listings.LISTING_SCREEN_PATTERN
import com.elfennani.aniwatch.presentation.screens.downloads.DOWNLOADS_SCREEN_PATTERN
import com.elfennani.aniwatch.presentation.screens.settings.SETTINGS_SCREEN_PATTERN
import com.elfennani.aniwatch.presentation.theme.AppTheme

sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : BottomNavScreen(HomeScreenPattern, "Home", icon = Icons.Rounded.Home)
    data object Listings :
        BottomNavScreen(LISTING_SCREEN_PATTERN, "My Lists", icon = Icons.AutoMirrored.Rounded.List)

    data object Downloads :
        BottomNavScreen(DOWNLOADS_SCREEN_PATTERN, "Downloads", icon = Icons.Rounded.Download)
}

val items = listOf(
    BottomNavScreen.Home,
    BottomNavScreen.Listings,
    BottomNavScreen.Downloads
)

@Composable
fun MainNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val density = LocalDensity.current
    val currentDestination = navBackStackEntry?.destination
    val destinationInList = items.any { screen ->
        currentDestination?.hierarchy?.any { it.route == screen.route } == true
    }
    if (destinationInList)
        NavigationBar(
            containerColor = AppTheme.colorScheme.primaryContainer,
            contentColor = AppTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .height(64.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
        ) {
            items.forEach { screen ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true;
                val color = AppTheme.colorScheme.onPrimaryContainer.copy(alpha = if(selected) 1f else 0.75f)

                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(indicatorColor = AppTheme.colorScheme.primaryAlt),
                    icon = { Icon(screen.icon, contentDescription = null, tint = color) },
//                    label = { Text(screen.label, style = AppTheme.typography.labelSmallBold, color = color) },
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
}