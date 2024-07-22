package com.elfennani.aniwatch.presentation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.elfennani.aniwatch.presentation.screens.home.HomeScreenPattern
import com.elfennani.aniwatch.presentation.screens.home.homeScreen

const val MainGraphPattern = "home"
fun NavGraphBuilder.mainGraph(navController: NavController){
    navigation(
        startDestination = HomeScreenPattern,
        route = MainGraphPattern
    ){
        homeScreen(navController)
    }
}