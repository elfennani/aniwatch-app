package com.elfennani.aniwatch.data.remote.converters

import com.elfennani.anilist.fragment.UserFragment
import com.elfennani.aniwatch.data.local.models.EmbeddedAnimeStats
import com.elfennani.aniwatch.data.local.models.EmbeddedMangaStats
import com.elfennani.aniwatch.data.local.models.LocalUser
import kotlin.time.Duration.Companion.minutes

fun UserFragment.asEntity() = LocalUser(
    id = id,
    icon = avatar?.medium,
    iconHD = avatar?.large,
    banner = bannerImage,
    name = name,
    bio = about,
    animeStats = with(statistics!!.anime!!) {
        EmbeddedAnimeStats(
            count = count,
            daysWatched = minutesWatched.toFloat() / (24 * 60),
            meanScore = meanScore.toFloat()
        )
    },
    mangaStats = with(statistics.manga!!) {
        EmbeddedMangaStats(
            count = count,
            readChapters = chaptersRead,
            meanScore = meanScore.toFloat()
        )
    }
)