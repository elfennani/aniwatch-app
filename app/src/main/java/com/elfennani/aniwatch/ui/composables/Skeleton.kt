package com.elfennani.aniwatch.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun Skeleton(modifier: Modifier = Modifier, animated: Boolean = true, content: @Composable () -> Unit = {}) {
    val transition = rememberInfiniteTransition(label = "")

    val transitionAnimation by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "",
    )

    Box(
        modifier = modifier.background(
            AppTheme.colorScheme.secondary.copy(
                alpha = if(animated) transitionAnimation else 0.5f
            ),
            RoundedCornerShape(4.dp)
        )
    )
}

@Preview
@Composable
private fun SkeletonPreview() {
    AppTheme {
        Skeleton()
    }
}