package com.elfennani.aniwatch.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@Composable
fun LoginScreen(
    onLogin: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Welcome to AniWatch!", textAlign = TextAlign.Center, fontSize = 24.sp, modifier = Modifier.fillMaxWidth())
                Text(text = "You need to log in to Anilist to continue", textAlign = TextAlign.Center, modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.75f))
            }

            Button(onClick = onLogin) {
                Text(text = "Login with Anilist")
            }
        }
    }
}

const val LoginScreenPattern = "auth/login"
fun NavGraphBuilder.loginScreen() {
    composable(LoginScreenPattern) {
        val loginViewModel: LoginViewModel = hiltViewModel()
        val context = LocalContext.current

        LoginScreen(
            onLogin = { loginViewModel.initiateLoginFlow(context) }
        )
    }
}

fun NavController.navigateToLoginScreen() {
    this.navigate(LoginScreenPattern){
        popUpTo(0){
            inclusive=true
        }
    }
}