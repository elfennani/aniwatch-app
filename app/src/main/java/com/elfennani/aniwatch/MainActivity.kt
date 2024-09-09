package com.elfennani.aniwatch

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.elfennani.aniwatch.ui.Navigation
import com.elfennani.aniwatch.ui.composables.MainNavigation
import com.elfennani.aniwatch.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()

                Scaffold(
                    containerColor = AppTheme.colorScheme.background,
                    contentColor = AppTheme.colorScheme.onBackground,
                    bottomBar = {
                        MainNavigation(navController = navController)
                    }
                ) {
                    Navigation(navController)
                }
            }
        }
    }
}