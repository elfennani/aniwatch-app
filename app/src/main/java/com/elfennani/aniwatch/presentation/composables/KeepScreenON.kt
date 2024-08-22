package com.elfennani.aniwatch.presentation.composables

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.elfennani.aniwatch.utils.requireActivity

@Composable
fun KeepScreenON(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    DisposableEffect(key1 = Unit) {
        val window = context.requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}