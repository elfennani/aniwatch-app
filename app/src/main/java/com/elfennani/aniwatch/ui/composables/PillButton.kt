package com.elfennani.aniwatch.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.ui.theme.AppTheme

@Composable
fun PillButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
) {
    Row(
        modifier = Modifier
            .border(1.dp, AppTheme.colorScheme.secondary.copy(0.2f), CircleShape)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(AppTheme.colorScheme.card, CircleShape)
            .padding(vertical = AppTheme.sizes.small, horizontal = AppTheme.sizes.normal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides AppTheme.colorScheme.onSecondary
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(AppTheme.sizes.medium)
            )
            Text(text = text, style = AppTheme.typography.labelSmallBold)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun PillButtonPrev() {
    AppTheme {
        Surface(
            color = AppTheme.colorScheme.background,
            contentColor = AppTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        ) {
            FlowRow(
                modifier = Modifier
                    .padding(AppTheme.sizes.medium),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal),
                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)
            ) {
                PillButton(onClick = {}, text = "Characters", icon = Icons.Default.Groups)
                PillButton(onClick = {}, text = "Edit Content", icon = Icons.Default.Edit)
                PillButton(onClick = {}, text = "Relations", icon = Icons.Default.AccountTree)
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PillButtonPrevDark() {
    PillButtonPrev()
}