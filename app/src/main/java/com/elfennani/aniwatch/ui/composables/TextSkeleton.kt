package com.elfennani.aniwatch.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun TextSkeleton(
    modifier: Modifier = Modifier,
    size: TextUnit = AppTheme.typography.body.lineHeight,
    animated: Boolean = true,
) {
    val transition = rememberInfiniteTransition(label = "")
    val lineHeightDp: Dp = with(LocalDensity.current) { size.toDp() }

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
        modifier = modifier
            .height(lineHeightDp)
            .background(
                AppTheme.colorScheme.secondary.copy(alpha = if (animated) transitionAnimation else 0.5f),
                RoundedCornerShape(4.dp)
            )
    )
}

@Composable
fun TextSkeleton(
    modifier: Modifier = Modifier,
    style: TextStyle = AppTheme.typography.body,
    animated: Boolean = true,
) {
    TextSkeleton(modifier, style.lineHeight, animated)
}