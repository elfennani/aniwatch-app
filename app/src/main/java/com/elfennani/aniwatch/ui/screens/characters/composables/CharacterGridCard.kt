package com.elfennani.aniwatch.ui.screens.characters.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.domain.models.Character
import com.elfennani.aniwatch.domain.models.VoiceActor
import com.elfennani.aniwatch.domain.models.enums.CharacterRole.Companion.value
import com.elfennani.aniwatch.ui.theme.AppTheme
import com.elfennani.aniwatch.utils.imageLoader
import com.elfennani.aniwatch.utils.plus

@Composable
fun CharacterGridCard(character: Character, showVA: Boolean = true) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.small),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            AsyncImage(
                model = character.imageSD,
                contentDescription = null,
                imageLoader = LocalContext.current.imageLoader(),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(0.69f)
                    .clip(AppTheme.shapes.thumbnail)
                    .background(AppTheme.colorScheme.secondary.copy(alpha = 0.2f))
            )

            Box(
                modifier = Modifier
                    .padding(AppTheme.sizes.small)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Black.copy(alpha = 0.2f))
                    .padding(AppTheme.sizes.smaller)
            ) {
                Text(
                    text = character.role.value,
                    style = AppTheme.typography.labelSmall,
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
        Text(
            text = character.name,
            style = AppTheme.typography.labelNormal,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedVisibility(
            showVA && character.voiceActors.isNotEmpty(),
            label = "va-${character.id}"
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = AppTheme.sizes.medium))
                character.voiceActors.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = it.image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(AppTheme.sizes.large)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(AppTheme.colorScheme.secondary.copy(alpha = 0.2f))
                                .align(Alignment.Top)
                        )
                        Text(text = it.name, style = AppTheme.typography.labelSmall, maxLines = 2)
                    }
                }
            }
        }
    }
}