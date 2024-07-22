package com.elfennani.aniwatch.presentation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.elfennani.aniwatch.presentation.screens.login.LoginScreenPattern
import com.elfennani.aniwatch.presentation.screens.login.loginScreen
import com.elfennani.aniwatch.presentation.screens.validate_token.validateTokenScreen

const val AuthGraphPattern = "auth"
fun NavGraphBuilder.authGraph(navController: NavController){
    navigation(
        startDestination = LoginScreenPattern,
        route = AuthGraphPattern
    ){
        loginScreen()
        validateTokenScreen(navController)
    }
}