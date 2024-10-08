package com.elfennani.aniwatch.data.remote.converters

import android.util.Log
import com.elfennani.anilist.fragment.ShowFragment
import com.elfennani.aniwatch.data.local.models.EmbeddedCover
import com.elfennani.aniwatch.data.local.models.EmbeddedDate
import com.elfennani.aniwatch.data.local.models.LocalShow
import com.elfennani.aniwatch.data.remote.converters.enums.asAppModel
import com.elfennani.aniwatch.domain.models.Tag
import kotlinx.datetime.Instant

fun ShowFragment.asEntity() = LocalShow(
    id = id,
    title = title?.userPreferred!!,
    description = description,
    genres = genres?.filterNotNull() ?: emptyList(),
    episodes = episodes,
    cover = with(coverImage!!) {
        EmbeddedCover(
            extraLarge = extraLarge!!,
            medium = medium!!,
            large = large!!,
            color = color
        )
    },
    season = season?.asAppModel(),
    year = seasonYear,
    format = format?.asAppModel()!!,
    banner = bannerImage,
    progress = mediaListEntry?.progress,
    status = mediaListEntry?.status?.asAppModel(),
    score = mediaListEntry?.score,
    state = status?.asAppModel(),
    type = type!!.asAppModel(),
    updatedAt = mediaListEntry?.updatedAt?.let { Instant.fromEpochSeconds(it.toLong()) },
    startedAt = mediaListEntry?.startedAt?.let {
        if(it.year == null || it.month == null || it.day == null) return@let null

        EmbeddedDate(year = it.year!!, month = it.month!!, day = it.day!!)
    },
    endedAt = mediaListEntry?.completedAt?.let {
        if(it.year == null || it.month == null || it.day == null) return@let null
        EmbeddedDate(year = it.year, month = it.month, day = it.day)
    },
    favorite = isFavourite,
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