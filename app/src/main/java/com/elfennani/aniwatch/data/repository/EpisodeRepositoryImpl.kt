package com.elfennani.aniwatch.data.repository

import android.util.Log
import androidx.compose.ui.util.fastRoundToInt
import androidx.room.withTransaction
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.elfennani.allanime.EpisodeQuery
import com.elfennani.allanime.InfoEpisodesQuery
import com.elfennani.allanime.SearchQuery
import com.elfennani.allanime.type.VaildTranslationTypeEnumType
import com.elfennani.anilist.ShowStreamingEpisodeQuery
import com.elfennani.aniwatch.data.local.AppDatabase
import com.elfennani.aniwatch.data.local.dao.EpisodeDao
import com.elfennani.aniwatch.data.local.models.LocalEpisode
import com.elfennani.aniwatch.data.local.models.asAppModel
import com.elfennani.aniwatch.di.AllAnimeApolloClient
import com.elfennani.aniwatch.di.AniListApolloClient
import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppErrorException
import com.elfennani.aniwatch.domain.models.Episode
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import com.elfennani.aniwatch.domain.models.EpisodeDetails
import com.elfennani.aniwatch.domain.repositories.EpisodeRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    @AllAnimeApolloClient private val allAnimeApolloClient: ApolloClient,
    @AniListApolloClient private val aniListApolloClient: ApolloClient,
    private val episodeDao: EpisodeDao,
    private val database: AppDatabase,
) : EpisodeRepository {

    override fun episodesByShowId(showId: Int): Flow<List<Episode>> = episodeDao
        .getListByShowId(showId)
        .map { it.map { ep -> ep.asAppModel() } }

    private suspend fun <T> retry(retries: Int = 3, action: suspend () -> T): T {
        var lastException: Throwable? = null

        repeat(retries) {
            try {
                return action()
            } catch (e: Exception) {
                lastException = e
            }
        }

        throw lastException!!
    }

    private data class AvailableEpisodes(
        val sub: List<String>,
        val dub: List<String>,
    )

    private data class VideoInfo(
        val vidResolution: Int,
        val vidPath: String,
        val vidSize: Long,
        val vidDuration: Float,
    )

    private data class SourceMap(
        val sourceUrl: String,
        val sourceName: String,
    )

    private fun convertToSourceUrls(data: Any?): List<SourceMap> {
        val list = data as List<*>

        return list.map {
            val sourceMap = it as Map<*, *>

            SourceMap(
                sourceUrl = sourceMap["sourceUrl"] as String,
                sourceName = sourceMap["sourceName"] as String,
            )
        }
    }

    private fun convertToVideoInfo(data: Any?): VideoInfo? {
        val map = data as? Map<*, *> ?: return null

        return VideoInfo(
            vidResolution = (map["vidResolution"] as? Int) ?: 0,
            vidPath = (map["vidPath"] as? String).orEmpty(),
            vidSize = (map["vidSize"] as? Number)?.toLong() ?: 0L,
            vidDuration = (map["vidDuration"] as? Number)?.toFloat() ?: 0f
        )
    }

    private fun convertAvailableEpisodesDetail(data: Any?): AvailableEpisodes? {
        val map = data as? Map<*, *> ?: return null

        return AvailableEpisodes(
            sub = (map["sub"] as? List<String>) ?: emptyList(),
            dub = (map["dub"] as? List<String>) ?: emptyList(),
        )
    }

    private suspend fun fetchAllAnimeShow(showId: Int): Pair<ApolloResponse<ShowStreamingEpisodeQuery.Data>, SearchQuery.Edge?> {
        val show = aniListApolloClient.query(ShowStreamingEpisodeQuery(showId)).execute()
        val showTitle = show.data?.media?.title?.userPreferred!!

        val searchResponse = retry {
            allAnimeApolloClient.query(SearchQuery(showTitle)).execute()
        }

        val allAnimeShow = searchResponse.data?.shows?.edges?.find {
            (it.aniListId as String).toInt() == showId
        }

        return Pair(show, allAnimeShow)
    }

    private suspend fun fetchAllAnimeEpisodeDetails(
        allAnimeShowId: String,
        episodes: Double,
    ): ApolloResponse<InfoEpisodesQuery.Data> {
        val episodes = retry {
            allAnimeApolloClient.query(
                InfoEpisodesQuery(allAnimeShowId, episodes)
            ).execute()
        }

        return episodes
    }

    override suspend fun fetchEpisodesByShowId(showId: Int) {
        withContext(Dispatchers.IO) {
            val (show, allAnimeShow) = fetchAllAnimeShow(showId)

            if (allAnimeShow != null) {
                val availableDetails = convertAvailableEpisodesDetail(
                    allAnimeShow.availableEpisodesDetail
                )!!
                val episodes = fetchAllAnimeEpisodeDetails(
                    allAnimeShow._id!!,
                    availableDetails.sub.size.toDouble()
                )


                val localEpisodes = availableDetails.sub.map { ep ->
                    val details = episodes.data?.episodeInfos?.find {
                        it!!.episodeIdNum.toString() == ep || it.episodeIdNum?.toInt()
                            .toString() == ep
                    }

                    val streamingEpisode = show.data?.media?.streamingEpisodes
                        ?.sortedBy { it?.title }
                        ?.find { it?.title?.startsWith("Episode $ep") ?: false }

                    LocalEpisode(
                        showId = showId,
                        episode = details?.episodeIdNum ?: ep.toDouble(),
                        dubbed = availableDetails.dub.contains(ep),
                        thumbnail = streamingEpisode?.thumbnail ?: details?.thumbnails
                            ?.filter { !(it?.contains("cdnfile") ?: false) }
                            ?.firstNotNullOf {
                                it?.let { path ->
                                    if (path.startsWith("http")) path
                                    else "$THUMBNAIL_PREFIX$path"
                                }
                            },
                        duration = convertToVideoInfo(details?.vidInforssub)?.vidDuration?.toInt(),
                        title = streamingEpisode?.title ?: "Episode $ep",
                        id = details?._id ?: "EP-$showId-$ep"
                    )
                }

                database.withTransaction {
                    episodeDao.deleteByShowId(showId)
                    episodeDao.upsert(localEpisodes)
                }
            }
        }
    }

    override suspend fun fetchEpisodeUrl(
        showId: Int,
        episode: Double,
        audio: EpisodeAudio,
        onlyMp4: Boolean,
    ): EpisodeDetails {
        return withContext(Dispatchers.IO){
            val (show, allAnimeShow) = fetchAllAnimeShow(showId)

            if (allAnimeShow != null) {
                val availableDetails = convertAvailableEpisodesDetail(
                    allAnimeShow.availableEpisodesDetail
                )!!

                val response = allAnimeApolloClient.query(
                    EpisodeQuery(
                        allAnimeShow._id!!,
                        VaildTranslationTypeEnumType.valueOf(audio.name.lowercase()),
                        episode.let {
                            if (it.fastRoundToInt().toDouble() == it)
                                it.fastRoundToInt().toString()
                            else
                                it.toString()
                        }
                    )
                ).execute()

                if (response.data == null) throw AppErrorException(AppError.NotFoundError)
                val sourceUrls = convertToSourceUrls(response.data?.episode?.sourceUrls)

                val (provider, url) = sourceUrls
                    .filter {
                        val providers = if (onlyMp4) MP4_PROVIDERS else PROVIDERS
                        providers.contains(it.sourceName)
                    }
                    .sortedBy { if (M3U8_PROVIDERS.contains(it.sourceName)) 0 else 1 }
                    .associate {
                        val baseUrl = "https://allanime.day"
                        val matches = Regex("/.{1,2}/g")
                            .findAll(it.sourceUrl.drop(2))
                            .toList()
                        val path = it.sourceUrl
                            .drop(2)
                            .chunked(2)
                            .map { value -> decrypt(value) }
                            .joinToString("")
                            .replace("clock", "clock.json")
                        Log.d("EpisodeRepositoryImpl", "fetchEpisodeUrl: $matches")
                        Log.d("EpisodeRepositoryImpl", "fetchEpisodeUrl: $path")
                        val sourceUrl = baseUrl + path

                        it.sourceName to sourceUrl
                    }
                    .loadURL() ?: throw AppErrorException(AppError.NotFoundError)

                if (!url.isNullOrEmpty())
                    return@withContext EpisodeDetails(
                        showId = showId,
                        audio = audio,
                        uri = url,
                        isStreaming = false,
                        isLocal = false,
                    )
            }

            throw AppErrorException(AppError.NotFoundError)
        }
    }

    private data class SourceData(
        val links: List<LinkData>,
    )

    private data class LinkData(
        val link: String?,
        val src: String?,
    )

    private fun Map<String, String>.loadURL(): Pair<String, String>? {
        val client = OkHttpClient()
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(SourceData::class.java)

        for ((name, url) in this) {
            val request = Request.Builder().url(url).build()

            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    Log.d("EpisodeRepositoryImpl", "loadURL: $url")

                    if (!responseBody.isNullOrEmpty()) {
                        val mediaUrl = adapter.fromJson(responseBody)?.links?.firstOrNull()
                        return Pair(name, mediaUrl?.link ?: mediaUrl?.src ?: return@use)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }

    private fun decrypt(string: String): Char {
        if (string == "01") return '9';
        if (string == "08") return '0';
        if (string == "05") return '=';
        if (string == "0a") return '2';
        if (string == "0b") return '3';
        if (string == "0c") return '4';
        if (string == "07") return '?';
        if (string == "00") return '8';
        if (string == "5c") return 'd';
        if (string == "0f") return '7';
        if (string == "5e") return 'f';
        if (string == "17") return '/';
        if (string == "54") return 'l';
        if (string == "09") return '1';
        if (string == "48") return 'p';
        if (string == "4f") return 'w';
        if (string == "0e") return '6';
        if (string == "5b") return 'c';
        if (string == "5d") return 'e';
        if (string == "0d") return '5';
        if (string == "53") return 'k';
        if (string == "1e") return '&';
        if (string == "5a") return 'b';
        if (string == "59") return 'a';
        if (string == "4a") return 'r';
        if (string == "4c") return 't';
        if (string == "4e") return 'v';
        if (string == "57") return 'o';
        if (string == "51") return 'i';
        if (string == "50") return 'h';
        if (string == "4b") return 's';
        if (string == "02") return ':';
        if (string == "55") return 'm';
        if (string == "4d") return 'u';
        if (string == "16") return '.';

        throw Exception("Unsupported character")
    }

    companion object {
        const val THUMBNAIL_PREFIX = "https://wp.youtube-anime.com/aln.youtube-anime.com"
        val M3U8_PROVIDERS = listOf("Luf-mp4", "Default");
        val MP4_PROVIDERS = listOf("S-mp4", "Kir", "Sak");
        val PROVIDERS = M3U8_PROVIDERS + MP4_PROVIDERS;
    }
}