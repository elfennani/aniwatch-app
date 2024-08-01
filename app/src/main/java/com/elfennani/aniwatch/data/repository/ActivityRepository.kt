package com.elfennani.aniwatch.data.repository

import android.util.Log
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.remote.models.asDomain
import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.Resource
import com.squareup.moshi.JsonDataException
import okio.IOException
import retrofit2.HttpException

class ActivityRepository(
    private val apiService: APIService,
) {
    suspend fun getFeed(page: Int): Resource<List<Activity>> {
        return try {
            Resource.Success(apiService.getFeedByPage(page).map { it.asDomain() })
        } catch (e: IOException) {
            Resource.Error(R.string.no_internet)
        } catch (e: JsonDataException) {
            Log.e("ActivityRepository", e.message.toString())
            Resource.Error(R.string.fail_parse)
        } catch (e: Exception) {
            Log.e("ActivityRepository", e.message.toString())
            Resource.Error()
        }
    }
}