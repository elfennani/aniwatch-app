package com.elfennani.aniwatch.ui.screens.validate_token

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.elfennani.aniwatch.ui.graphs.navigateToMainGraph

@Composable
fun ValidateTokenScreen(errors: List<Int>) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (errors.isEmpty())
                CircularProgressIndicator()
            errors.forEach { error ->
                Text(
                    text = stringResource(error),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

const val ValidateTokenScreenPattern = "auth/validate/{params}"
fun NavGraphBuilder.validateTokenScreen(navController: NavController) {
    composable(
        ValidateTokenScreenPattern,
        deepLinks = listOf(navDeepLink { uriPattern = "aniwatch://redirect#{params}" })
    ) {
        val viewModel: ValidateTokenViewModel = hiltViewModel();
        val errors by viewModel.errors.collectAsState()
        val context = LocalContext.current

        LaunchedEffect(key1 = "") {
            viewModel.validate(context) {
                navController.navigateToMainGraph(true)
            }
        }

        ValidateTokenScreen(
            errors = errors
        )
    }
}