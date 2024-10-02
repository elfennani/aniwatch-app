package com.elfennani.aniwatch.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.apollographql.apollo.ApolloClient
import com.elfennani.allanime.InfoEpisodesQuery
import com.elfennani.allanime.SearchQuery
import com.elfennani.anilist.ShowStreamingEpisodeQuery
import com.elfennani.aniwatch.data.local.AppDatabase
import com.elfennani.aniwatch.data.local.dao.EpisodeDao
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.models.LocalEpisode
import com.elfennani.aniwatch.data.local.models.asAppModel
import com.elfennani.aniwatch.di.AllAnimeApolloClient
import com.elfennani.aniwatch.di.AniListApolloClient
import com.elfennani.aniwatch.domain.models.Episode
import com.elfennani.aniwatch.domain.repositories.EpisodeRepository
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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

    override suspend fun fetchEpisodesByShowId(showId: Int) {
        withContext(Dispatchers.IO) {
            val show = aniListApolloClient.query(ShowStreamingEpisodeQuery(showId)).execute()
            val showTitle = show.data?.media?.title?.userPreferred!!

            val searchResponse = retry {
                allAnimeApolloClient.query(SearchQuery(showTitle)).execute()
            }

            val allAnimeShow = searchResponse.data?.shows?.edges?.find {
                (it.aniListId as String).toInt() == showId
            }

            if (allAnimeShow != null) {
                val availableDetails =
                    convertAvailableEpisodesDetail(allAnimeShow.availableEpisodesDetail)!!

                val episodes = retry {
                    allAnimeApolloClient.query(
                        InfoEpisodesQuery(
                            allAnimeShow._id!!,
                            availableDetails.sub.size.toDouble()
                        )
                    ).execute()
                }

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

    companion object {
        const val THUMBNAIL_PREFIX = "https://wp.youtube-anime.com/aln.youtube-anime.com"
    }
}