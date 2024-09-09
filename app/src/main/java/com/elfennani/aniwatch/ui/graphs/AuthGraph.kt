package com.elfennani.aniwatch.ui.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.elfennani.aniwatch.ui.screens.login.LoginScreenPattern
import com.elfennani.aniwatch.ui.screens.login.loginScreen
import com.elfennani.aniwatch.ui.screens.validate_token.validateTokenScreen

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