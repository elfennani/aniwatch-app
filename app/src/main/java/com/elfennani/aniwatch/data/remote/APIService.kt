package com.elfennani.aniwatch.data.remote


import com.elfennani.aniwatch.data.remote.models.NetworkEpisodeLink
import com.elfennani.aniwatch.data.remote.models.NetworkShowBasic
import com.elfennani.aniwatch.data.remote.models.SerializableShowDetails
import com.elfennani.aniwatch.data.remote.models.SerializableShowStatus
import com.elfennani.aniwatch.data.remote.models.TranslationNetwork
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/shows/{status}")
    suspend fun getShowsByStatus(
        @Path("status") status: SerializableShowStatus,
        @Query("all") all: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("userId") userId: Int? = null
    ): List<NetworkShowBasic>

    @GET("/show/{id}")
    suspend fun getShowById(@Path("id") id: Int): SerializableShowDetails

    @GET("/episode/{allAnimeId}/{episode}")
    suspend fun getEpisodeById(
        @Path("allAnimeId") allAnimeId: String,
        @Path("episode") episode: Int,
        @Query("type") type: TranslationNetwork = TranslationNetwork.SUB
    ): NetworkEpisodeLink
}