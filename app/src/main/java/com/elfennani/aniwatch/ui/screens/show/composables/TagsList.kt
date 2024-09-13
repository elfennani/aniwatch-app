package com.elfennani.aniwatch.ui.screens.show.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.elfennani.aniwatch.models.Tag
import com.elfennani.aniwatch.ui.composables.PillButton
import com.elfennani.aniwatch.ui.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsList(
    tags: List<Tag>,
) {
    var spoilers by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(horizontal = AppTheme.sizes.large)
            .padding(bottom = AppTheme.sizes.large),
        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Tags",
                style = AppTheme.typography.labelLarge,
            )
            AnimatedContent(
                targetState = spoilers,
                label = "spoilers"
            ) { state ->
                PillButton(
                    onClick = { spoilers = !spoilers },
                    text = if (state) "Hide Spoil" else "Show Spoiler",
                    icon = Icons.Default.Language
                )
            }
        }
        AnimatedContent(targetState = spoilers, label = "") { spoilers ->
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                tags.let {
                    if (!spoilers)
                        it.filter { tag -> !tag.spoiler }
                    else
                        it
                }.forEach { tag ->
                    TagCard(tag)
                }
            }
        }
    }
}

@Composable
private fun TagCard(tag: Tag) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(
                if (tag.spoiler) AppTheme.colorScheme.primaryContainer
                else AppTheme.colorScheme.card
            )
            .padding(AppTheme.sizes.small),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            tag.label,
            style = AppTheme.typography.labelSmall,
        )
        Text(
            text = tag.percentage.toString() + "%",
            style = AppTheme.typography.labelSmallBold,
            color = AppTheme.colorScheme.primary,
        )
    }
}