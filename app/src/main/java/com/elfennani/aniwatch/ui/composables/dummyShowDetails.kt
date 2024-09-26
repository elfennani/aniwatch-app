package com.elfennani.aniwatch.ui.composables

import androidx.compose.ui.graphics.Color
import com.elfennani.aniwatch.domain.models.DownloadState
import com.elfennani.aniwatch.domain.models.Episode
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.ShowImage
import com.elfennani.aniwatch.domain.models.enums.ShowSeason
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.models.Tag

val dummyShow
    get() = Show(
        id = 109731,
        allanimeId = "oso3xi8Knpv5BKqem",
        name = "Sound! Euphonium 3",
        description = "The third season of <i>Hibike! Euphonium</i>.<br><br>Kumiko's third year finally begins! With the concert band at Kitauji High School over 90 members, Kumiko is now the president and does her best with her final high school club activities to try to win her long-desired gold at nationals.<br><br>(Source: Crunchyroll, edited)",
        episodesCount = 13,
        episodes = listOf(
            Episode(
                id = "xvxSS5Mzdt9MtEh5R",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 3.0,
                name = "Ep 3",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/3_sub.jpg",
                duration = 1497,
                state = DownloadState.NotSaved
            ),
            Episode(
                id = "Ld577KNfZTXY8XT5u",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 2.0,
                name = "Ep 2",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/2_sub.jpg",
                duration = 1495,
                state = DownloadState.Pending
            ),
            Episode(
                id = "iCwfjXZJzDHr74Xw8",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 1.0,
                name = "Ep 1",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/1_sub.jpg",
                duration = 1497,
                state = DownloadState.Downloading(0.32f)
            ),
            Episode(
                id = "G4J7aZMjrh2eZmSvi",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 5.0,
                name = "Ep 5",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/5_sub.jpg",
                duration = 1497,
                state = DownloadState.NotSaved
            ),
            Episode(
                id = "ARBy6J4cn8qezHGF3",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 4.0,
                name = "Ep 4",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/4_sub.jpg",
                duration = 1497,
                state = DownloadState.NotSaved
            ),
            Episode(
                id = "SJRLPXTEGM6e8gCsq",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 6.0,
                name = "Ep 6",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/6_sub.jpg",
                duration = 1497,
                state = DownloadState.NotSaved
            ),
            Episode(
                id = "hZ7m9P5nBgTuTgeXw",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 9.0,
                name = "Ep 9",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/9_sub.jpg",
                duration = 1497,
                state = DownloadState.NotSaved
            ),
            Episode(
                id = "mMpFXSniTN876SdaL",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 8.0,
                name = "Ep 8",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/8_sub.jpg",
                duration = 1497,
                state = DownloadState.NotSaved
            ),
            Episode(
                id = "mARMTfbc8JWDpBqrK",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 7.0,
                name = "Ep 7",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/7_sub.jpg",
                duration = 1497,
                state = DownloadState.NotSaved
            ),
            Episode(
                id = "4WtFCFWjpJLs9tGbm",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 12.0,
                name = "Ep 12",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/12_sub.jpg",
                duration = 1497,
                state = DownloadState.NotSaved
            ),
            Episode(
                id = "33MQMKfj29uni4mmA",
                allanimeId = "oso3xi8Knpv5BKqem",
                animeId = 109731,
                episode = 13.0,
                name = "Ep 13",
                dubbed = false,
                thumbnail = "https://wp.youtube-anime.com/aln.youtube-anime.com/data2/ep_tbs/oso3xi8Knpv5BKqem/13_sub.jpg",
                duration = 1497,
                state = DownloadState.NotSaved
            )
        ),
        genres = listOf("Drama", "Music", "Slice of Life"),
        tags = listOf(
            Tag(
                id = 98,
                label = "Female Protagonist",
                percentage = 97,
                spoiler = false,
                description = it.description
            ),
            Tag(
                id = 110,
                label = "Band",
                percentage = 94,
                spoiler = false,
                description = it.description
            ),
            Tag(
                id = 84,
                label = "School Club",
                percentage = 94,
                spoiler = false,
                description = it.description
            ),
            Tag(
                id = 102,
                label = "Coming of Age",
                percentage = 89,
                spoiler = false,
                description = it.description
            ),
            Tag(
                id = 86,
                label = "Primarily Female Cast",
                percentage = 85,
                spoiler = false,
                description = it.description
            ),
            Tag(
                id = 1673,
                label = "Classical Music",
                percentage = 85,
                spoiler = false,
                description = it.description
            ),
            Tag(
                id = 1228,
                label = "Primarily Teen Cast",
                percentage = 83,
                spoiler = false,
                description = it.description
            ),
            Tag(
                id = 46,
                label = "School",
                percentage = 65,
                spoiler = false,
                description = it.description
            ),
            Tag(
                id = 294,
                label = "Bisexual",
                percentage = 60,
                spoiler = false,
                description = it.description
            ),
            Tag(
                id = 153,
                label = "Time Skip",
                percentage = 53,
                spoiler = true,
                description = it.description
            )
        ),
        season = ShowSeason.SPRING,
        year = 2024,
        format = "TV",
        image = ShowImage(
            large = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx109731-PBjrX9CebGus.png",
            medium = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx109731-PBjrX9CebGus.png",
            original = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx109731-PBjrX9CebGus.png",
            color = Color(0xFFe4c95d)
        ),
        banner = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/109731-O9wpjWphEhKn.jpg",
        progress = 5,
        status = ShowStatus.WATCHING
    )