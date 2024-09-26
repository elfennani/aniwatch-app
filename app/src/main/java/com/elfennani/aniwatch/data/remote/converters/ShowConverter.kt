package com.elfennani.aniwatch.data.remote.converters

import com.elfennani.anilist.fragment.ShowFragment
import com.elfennani.aniwatch.data.local.models.LocalShow
import com.elfennani.aniwatch.domain.models.Tag

fun ShowFragment.asEntity() = LocalShow(
    id = id,
    title = title?.userPreferred!!,
    description = description,
    genres = genres?.filterNotNull() ?: emptyList(),
    episodes = episodes,
    cover = coverImage?.large!!,
    season = season?.asAppModel(),
    year = seasonYear,
    format = format?.asAppModel()!!,
    banner = bannerImage,
    progress = mediaListEntry?.progress,
    status = mediaListEntry?.status?.asAppModel(),
    tags = tags?.map {
        Tag(
            id = it!!.id,
            label = it.name,
            percentage = it.rank ?: -1,
            spoiler = it.isMediaSpoiler!! || it.isGeneralSpoiler!!,
            description = it.description
        )
    } ?: emptyList()
)