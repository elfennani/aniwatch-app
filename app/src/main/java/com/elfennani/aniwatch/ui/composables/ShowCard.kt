package com.elfennani.aniwatch.ui.composables

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.ShowImage
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.models.enums.formatText
import com.elfennani.aniwatch.domain.models.enums.toIcon
import com.elfennani.aniwatch.ui.theme.AppTheme
import com.elfennani.aniwatch.utils.imageLoader

@Composable
fun ShowCard(
    show: Show,
    subtitle: String? = null,
    overlay: @Composable BoxScope.() -> Unit = {},
    onClick: () -> Unit = {},
) {
    val imageLoader = LocalContext.current.imageLoader()
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(horizontal = AppTheme.sizes.large, vertical = AppTheme.sizes.normal),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
    ) {
        Box {
            AsyncImage(
                model = show.image.large,
                contentDescription = null,
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(80.dp)
                    .clip(AppTheme.shapes.thumbnail)
                    .background(AppTheme.colorScheme.secondary.copy(alpha = 0.2f))
                    .aspectRatio(0.69f)
            )

            overlay()
        }
        Column(
            modifier = Modifier
                .padding(vertical = AppTheme.sizes.normal),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
        ) {
            Text(text = show.name, style = AppTheme.typography.labelLarge, maxLines = 2)
            Text(
                text = subtitle ?: "${show.episodes} Episodes",
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(AppTheme.sizes.small))
            if (show.status != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)
                ) {
                    Icon(
                        imageVector = show.status.toIcon(),
                        contentDescription = null,
                        tint = AppTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = show.status.formatText(),
                        style = AppTheme.typography.labelNormal,
                        color = AppTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}