package com.elfennani.aniwatch.data.remote


import com.elfennani.aniwatch.data.remote.models.NetworkActivity
import com.elfennani.aniwatch.data.remote.models.NetworkCharacter
import com.elfennani.aniwatch.data.remote.models.NetworkEpisodeLink
import com.elfennani.aniwatch.data.remote.models.NetworkRelation
import com.elfennani.aniwatch.data.remote.models.NetworkShowBasic
import com.elfennani.aniwatch.data.remote.models.NetworkStatusDetails
import com.elfennani.aniwatch.data.remote.models.NetworkUser
import com.elfennani.aniwatch.data.remote.models.PagingResource
import com.elfennani.aniwatch.data.remote.models.SerializableShowDetails
import com.elfennani.aniwatch.data.remote.models.SerializableShowStatus
import com.elfennani.aniwatch.data.remote.models.TranslationNetwork
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface APIService {
    @GET("/shows/{status}")
    suspend fun getShowsByStatus(
        @Path("status") status: SerializableShowStatus,
        @Query("userId") userId: Int? = null,
    ): List<NetworkShowBasic>

    @GET("/show/{id}")
    suspend fun getShowById(@Path("id") id: Int): SerializableShowDetails

    @GET("/episode/{allAnimeId}/{episode}")
    suspend fun getEpisodeById(
        @Path("allAnimeId") allAnimeId: String,
        @Path("episode") episode: Int,
        @Query("type") type: TranslationNetwork = TranslationNetwork.SUB,
    ): NetworkEpisodeLink

    @GET("/feed/{page}")
    suspend fun getFeedByPage(
        @Path("page") page: Int,
        @Query("userId") userId: Int? = null,
    ): List<NetworkActivity>

    @GET("/show/status/{id}")
    suspend fun getStatusDetailsById(@Path("id") id: Int): NetworkStatusDetails

    @POST("/show/status/{id}")
    suspend fun setStatusDetailsById(@Path("id") id: Int, @Body statusDetails: NetworkStatusDetails)

    @DELETE("/show/status/{id}")
    suspend fun deleteStatusDetailsById(@Path("id") id: Int)

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>

    @GET("/user/{id}")
    suspend fun getUserById(@Path("id") id: Int): NetworkUser

    @GET("/user")
    suspend fun getViewerUser(): NetworkUser

    @GET("/search")
    suspend fun searchShowsByQuery(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): PagingResource<NetworkShowBasic>

    @GET("/characters/{showId}")
    suspend fun getCharactersByShowId(
        @Path("showId") showId: Int,
        @Query("page") page: Int,
    ): PagingResource<NetworkCharacter>

    @GET("/relations/{showId}")
    suspend fun getRelationsByShowId(@Path("showId") showId: Int): List<NetworkRelation>
}