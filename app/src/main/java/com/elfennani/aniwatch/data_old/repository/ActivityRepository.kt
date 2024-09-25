package com.elfennani.aniwatch.data_old.repository

import android.util.Log
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data_old.remote.APIService
import com.elfennani.aniwatch.data_old.remote.models.asDomain
import com.elfennani.aniwatch.domain.models.Resource
import com.squareup.moshi.JsonDataException
import okio.IOException

class ActivityRepository(
    private val apiService: APIService,
) {
    suspend fun getFeed(page: Int): Resource<List<com.elfennani.aniwatch.domain.models.Activity>> {
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