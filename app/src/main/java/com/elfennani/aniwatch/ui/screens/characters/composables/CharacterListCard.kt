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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.elfennani.aniwatch.domain.models.Character
import com.elfennani.aniwatch.domain.models.VoiceActor
import com.elfennani.aniwatch.ui.theme.AppTheme
import com.elfennani.aniwatch.utils.imageLoader
import com.elfennani.aniwatch.utils.plus

@Composable
fun CharacterListCard(character: Character, showVA: Boolean = true) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = character.imageSD,
            contentDescription = null,
            imageLoader = LocalContext.current.imageLoader(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(96.dp)
                .aspectRatio(0.69f)
                .clip(AppTheme.shapes.thumbnail)
                .background(AppTheme.colorScheme.secondary.copy(alpha = 0.2f))
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(AppTheme.sizes.normal),
            verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.smaller)
            ) {
                Text(
                    text = character.name,
                    style = AppTheme.typography.titleNormal,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = character.role,
                    style = AppTheme.typography.labelSmall
                )
            }
            AnimatedVisibility(
                showVA && character.voiceActor.isNotEmpty(),
                label = "va-${character.id}"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.small)) {
                    HorizontalDivider()
                    character.voiceActor.forEach {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.small),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = it.image,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(AppTheme.sizes.large * 1.15f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(AppTheme.colorScheme.secondary.copy(alpha = 0.2f))
                                    .align(Alignment.Top)
                            )
                            Text(
                                text = it.name,
                                style = AppTheme.typography.labelSmall,
                                maxLines = 2,
                                modifier = Modifier.weight(1f)
                            )
                            if (!it.language.isNullOrEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            AppTheme.colorScheme.primary,
                                            RoundedCornerShape(2.dp)
                                        )
                                        .padding(
                                            horizontal = AppTheme.sizes.smaller,
                                            vertical = 2.dp
                                        )
                                ) {
                                    Text(
                                        text = it.language.slice(indices = IntRange(0, 2))
                                            .uppercase(),
                                        style = AppTheme.typography.labelSmallBold,
                                        color = AppTheme.colorScheme.onPrimary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CharacterListCardPrev() {
    val character = Character(
        id = 283788,
        name = "Kumiko Oumae",
        image = "https://s4.anilist.co/file/anilistcdn/character/large/b88708-ZiVPl8LjIjaK.jpg",
        imageSD = "https://s4.anilist.co/file/anilistcdn/character/medium/b88708-ZiVPl8LjIjaK.jpg",
        role = "MAIN",
        voiceActor = listOf(
            VoiceActor(
                id = 106661,
                name = "Tomoyo Kurosawa",
                image = "https://s4.anilist.co/file/anilistcdn/staff/medium/n106661-r5f4gxJEyU67.jpg",
                language = "Japanese"
            ),

            )
    )

    AppTheme {
        Scaffold(
            containerColor = AppTheme.colorScheme.background,
            contentColor = AppTheme.colorScheme.onBackground
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium),
                contentPadding = it.plus(PaddingValues(AppTheme.sizes.medium))
            ) {
                val characters = listOf(character, character, character, character)
                items(characters) {
                    Column(
                        modifier = Modifier
                    ) {
                        CharacterListCard(character = character)
                    }
                }
            }
        }
    }
}