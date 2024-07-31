package com.elfennani.aniwatch.presentation.screens.show.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.imageLoader
import com.elfennani.aniwatch.presentation.composables.Divider
import com.elfennani.aniwatch.presentation.composables.Skeleton
import com.elfennani.aniwatch.presentation.composables.TextSkeleton
import com.elfennani.aniwatch.presentation.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShowScreenSkeleton(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    onBack: () -> Unit = {},
) {
    val background = AppTheme.colorScheme.background

    Column {
        Box {
            Skeleton(
                modifier = Modifier
                    .matchParentSize()
                    .drawWithContent {
                        val colors = listOf(background.copy(0f), background)
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors,
                                startY = 0f,
                            ),
                            blendMode = BlendMode.SrcOver
                        )
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
                Skeleton(
                    modifier = Modifier
                        .clip(AppTheme.shapes.thumbnail)
                        .width(128.dp)
                        .aspectRatio(0.69f)
                )
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = AppTheme.sizes.large),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)
            ) {
                (0..2).forEach { _ ->
                    TextSkeleton(
                        style = AppTheme.typography.labelSmallBold,
                        modifier = Modifier
                            .width(64.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .padding(vertical = AppTheme.sizes.smaller * 1.25f)
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)) {
                TextSkeleton(
                    style = AppTheme.typography.titleNormal,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                TextSkeleton(
                    style = AppTheme.typography.labelSmall,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
            }

            Skeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
            ) {
                TextSkeleton(
                    style = AppTheme.typography.labelNormal,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                )
                TextSkeleton(
                    style = AppTheme.typography.labelNormal,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextSkeleton(
                    style = AppTheme.typography.labelNormal,
                    modifier = Modifier
                        .fillMaxWidth(0.66f)
                )
            }

            Divider()
        }
    }
}

@Preview
@Composable
private fun ShowScreenSkeletonPreview() {
    AppTheme {
        Scaffold(
            contentColor = AppTheme.colorScheme.onBackground,
            containerColor = AppTheme.colorScheme.background
        ) {
            ShowScreenSkeleton(padding = it)
        }
    }
}