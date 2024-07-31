package com.elfennani.aniwatch.presentation.screens.show.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.imageLoader
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun ShowScreenHeaderBanner(
    banner: String?,
    showImage: String,
    lazyListState: LazyListState,
    padding: PaddingValues,
    onBack: () -> Unit,
) {
    val background = AppTheme.colorScheme.background
    val context = LocalContext.current
    var scrolledY = 0f
    var previousOffset = 0

    Box {
        AsyncImage(
            model = banner,
            contentDescription = null,
            imageLoader = context.imageLoader(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .background(AppTheme.colorScheme.secondary.copy(0.5f))
                .graphicsLayer {
                    scrolledY += lazyListState.firstVisibleItemScrollOffset - previousOffset
                    translationY = scrolledY * 0.5f
                    previousOffset = lazyListState.firstVisibleItemScrollOffset
                }
                .drawWithCache {
                    val colors = listOf(background.copy(0f), background)
                    val gradient = Brush.verticalGradient(
                        colors,
                        startY = 0f,
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            brush = gradient,
                            blendMode = BlendMode.SrcOver
                        )
                    }
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = padding.calculateTopPadding())
                .padding(
                    horizontal = padding.calculateStartPadding(
                        LayoutDirection.Ltr
                    )
                )
                .padding(AppTheme.sizes.large),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.large * 3),
            horizontalAlignment = Alignment.Start
        ) {
            FilledIconButton(
                colors = IconButtonDefaults
                    .filledIconButtonColors()
                    .copy(
                        containerColor = AppTheme.colorScheme.onBackground.copy(
                            alpha = 0.12f
                        )
                    ),
                modifier = Modifier.offset(x = (-4).dp),
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            AsyncImage(
                model = showImage,
                contentDescription = null,
                imageLoader = context.imageLoader(),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(AppTheme.shapes.thumbnail)
                    .background(AppTheme.colorScheme.secondary.copy(0.5f))
                    .width(128.dp)
                    .aspectRatio(0.69f)
            )
        }
    }
}