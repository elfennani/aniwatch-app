package com.elfennani.aniwatch.ui.screens.home.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.aniwatch.ui.composables.Skeleton
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun WatchingCardSkeleton(animated: Boolean = true) {
    val context = LocalContext.current
    val lineHeightSp: TextUnit = 18.sp
    val lineHeightDp: Dp = with(LocalDensity.current) {
        lineHeightSp.toDp()
    }


    Column(
        modifier = Modifier
            .clip(AppTheme.shapes.thumbnail)
            .width(256.dp),
    ) {
        Skeleton(
            modifier = Modifier
                .aspectRatio((21 / 9f))
                .fillMaxWidth()
                .clip(AppTheme.shapes.thumbnail),
            animated = animated
        )
        Box(Modifier.padding(vertical = AppTheme.sizes.small)) {
            Skeleton(
                Modifier
                    .height(lineHeightDp)
                    .width(140.dp)
                    .clip(AppTheme.shapes.thumbnail),
                animated = animated
            )
        }
    }
}
