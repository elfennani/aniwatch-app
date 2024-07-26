package com.elfennani.aniwatch.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun Section(modifier: Modifier = Modifier, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = title, style = AppTheme.typography.titleNormal, color = AppTheme.colorScheme.onBackground)
    }
}

@Preview
@Composable
private fun SectionPreview() {
    AppTheme {
        Scaffold(
            contentColor = AppTheme.colorScheme.onBackground,
            containerColor = AppTheme.colorScheme.background,
            modifier = Modifier.height(256.dp)
        ) {
            Column(modifier = Modifier
                .padding(it)
                .padding(AppTheme.sizes.medium)) {
                Section(title = "Continue Watching")
            }
        }
    }
}