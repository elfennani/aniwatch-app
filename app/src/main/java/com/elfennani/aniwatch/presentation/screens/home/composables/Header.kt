package com.elfennani.aniwatch.presentation.screens.home.composables

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.utils.imageLoader
import com.elfennani.aniwatch.models.User
import com.elfennani.aniwatch.presentation.theme.AppTheme

private const val DIVIDER_FADE_DURATION = 150

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    border: Boolean = false,
    user: User?,
    onSearch: () -> Unit,
) {
    val imageLoader = LocalContext.current.imageLoader()

    val logoResource = with(isSystemInDarkTheme()) {
        if (this)
            R.drawable.aniwatch_dark_mode
        else
            R.drawable.aniwatch_light_mode
    }


    Column {
        Row(
            modifier = Modifier
                .padding(AppTheme.sizes.medium)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = logoResource),
                contentDescription = null,
                modifier = Modifier.height(AppTheme.sizes.medium * 1.25f)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onSearch() }
                        .background(AppTheme.colorScheme.card)
                        .size(AppTheme.sizes.large * 2),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = AppTheme.colorScheme.primary
                    )
                }

                AsyncImage(
                    model = user?.icon,
                    contentDescription = null,
                    imageLoader = imageLoader,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { }
                        .background(AppTheme.colorScheme.secondary.copy(if(user?.icon == null) 0.25f else 0f))
                        .size(AppTheme.sizes.large*2)
                )
            }

        }
        AnimatedVisibility(
            visible = border,
            enter = fadeIn(tween(DIVIDER_FADE_DURATION)),
            exit = fadeOut(tween(DIVIDER_FADE_DURATION))
        ) {
            HorizontalDivider(color = AppTheme.colorScheme.onBackground.copy(.07f))
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun HomeHeaderPrev() {
    AppTheme {
        Scaffold(
            containerColor = AppTheme.colorScheme.background,
            contentColor = AppTheme.colorScheme.onBackground,
            modifier =  Modifier.height(256.dp)
        ) {
            HomeHeader(
                user = null,
                onSearch = {}
            )
        }
    }
}