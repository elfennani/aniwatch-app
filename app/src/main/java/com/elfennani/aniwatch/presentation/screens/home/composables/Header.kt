package com.elfennani.aniwatch.presentation.screens.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun Header(
    onPressSearch: ()->Unit = {}
){
    val isDarkTheme = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppTheme.sizes.large * 3, bottom = AppTheme.sizes.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.large)
    ) {
        Image(
            painter = painterResource(id = if(isDarkTheme) R.drawable.logo_dark_mode else R.drawable.logo_light_mode),
            contentDescription = null,
            modifier = Modifier.sizeIn(maxWidth = 96.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AppTheme.shapes.input)
                .clickable { onPressSearch() }
                .background(AppTheme.colorScheme.card, AppTheme.shapes.input)
                .padding(
                    horizontal = AppTheme.sizes.medium,
                    vertical = AppTheme.sizes.normal
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal),
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = AppTheme.colorScheme.onSecondary
            )
            Text(
                text = "Attack on Titan...",
                style = AppTheme.typography.body,
                color = AppTheme.colorScheme.secondary
            )
        }
    }
}