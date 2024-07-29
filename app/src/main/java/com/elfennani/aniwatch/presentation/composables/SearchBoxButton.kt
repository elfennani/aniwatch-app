package com.elfennani.aniwatch.presentation.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun SearchBoxButton(modifier: Modifier = Modifier) {
    val shape = AppTheme.shapes.input

    Row(
        modifier = modifier
            .shadow(AppTheme.sizes.smaller,shape)
            .clip(shape)
            .background(AppTheme.colorScheme.card, shape)
            .clickable {  }
            .fillMaxWidth()
            .padding(horizontal = AppTheme.sizes.medium, vertical = AppTheme.sizes.normal),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = AppTheme.colorScheme.secondary, modifier = Modifier.size(AppTheme.sizes.large))
        Text(text = "Attack on Titan S3 ...", style = AppTheme.typography.labelNormal.copy(fontWeight = FontWeight.Normal), color = AppTheme.colorScheme.secondary)
    }
}

@Preview
@Composable
private fun SearchBoxButtonPrev() {
    AppTheme {
        Surface(
            contentColor = AppTheme.colorScheme.onBackground
        ) {
            Column(
                modifier = Modifier
                    .background(AppTheme.colorScheme.background)
                    .padding(AppTheme.sizes.normal)
                    .width(411.dp)
            ) {
                SearchBoxButton()
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchBoxPrevDarkMode() {
    SearchBoxButtonPrev()
}