package com.elfennani.aniwatch.data.remote.converters

import com.elfennani.anilist.fragment.CharacterFragment
import com.elfennani.aniwatch.data.local.models.LocalCharacter
import com.elfennani.aniwatch.data.remote.converters.enums.asAppModel
import com.elfennani.aniwatch.domain.models.VoiceActor

fun CharacterFragment.asEntity(showId: Int) = LocalCharacter(
    id = node!!.id,
    name = node.name?.userPreferred!!,
    role = role?.asAppModel()!!,
    icon = node.image?.medium,
    iconHD = node.image?.large,
    showId = showId,
    voiceActors = voiceActors
        ?.map {
            VoiceActor(
                id = it!!.id,
                name = it.name?.userPreferred!!,
                image = it.image?.medium,
                language = it.languageV2
            )
        } ?: emptyList()
)