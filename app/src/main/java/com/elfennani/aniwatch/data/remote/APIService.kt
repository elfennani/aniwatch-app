package com.elfennani.aniwatch.data.remote


import com.elfennani.aniwatch.data.remote.models.SerializableShowBasic
import com.elfennani.aniwatch.data.remote.models.SerializableShowDetails
import com.elfennani.aniwatch.data.remote.models.SerializableShowStatus
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/shows/{status}")
    suspend fun getShowsByStatus(@Path("status") status: SerializableShowStatus, @Query("page") page: Int = 1,@Query("userId") userId: Int? = null): List<SerializableShowBasic>

    @GET("/show/{id}")
    suspend fun getShowById(@Path("id") id: Int): SerializableShowDetails

}