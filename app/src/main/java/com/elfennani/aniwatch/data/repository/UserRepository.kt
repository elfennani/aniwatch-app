package com.elfennani.aniwatch.data.repository

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data.local.dao.CachedUserDao
import com.elfennani.aniwatch.data.local.entities.toDomain
import com.elfennani.aniwatch.data.local.entities.toEntity
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.remote.models.toDomain
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.models.User
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.Instant
import java.util.concurrent.TimeUnit

class UserRepository(
    private val apiService: APIService,
    private val cachedUserDao: CachedUserDao,
    private val context: Context
) {

    private val viewerKey = intPreferencesKey("VIEWER_KEY")

    private fun cacheExpireKey(id:Int) = longPreferencesKey("USER_EXPIRE_$id")
    private fun Preferences.getUserExpiration(id:Int) = this[cacheExpireKey(id)]

    suspend fun userFlow(id:Int) = flow{
        emit(Resource.Success(cachedUserDao.getUser(id)?.toDomain()))
        val lastFetch = context.dataStore.data.first().getUserExpiration(id) ?: 0L
        val hour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val current = Instant.now().toEpochMilli()

        if(current - lastFetch > hour){
            val result = fetchUser(id)
            if(result is Resource.Error){
                emit(Resource.Error(result.message!!))
            }
        }

        emitAll(cachedUserDao.getUserFlow(id).map { Resource.Success(it?.toDomain()) })
    }

    suspend fun viewerFlow() = flow {
        var viewerId = context.dataStore.data.first()[viewerKey]

        if(viewerId == null){
            val res = fetchViewer()
            if(res is Resource.Error){
                emit(res)
                return@flow
            }else{
                viewerId = res.data?.id
            }
        }

        emitAll(userFlow(viewerId!!))
    }

    suspend fun fetchViewer(): Resource<User>{
        return try {
            val result = apiService.getViewerUser().toDomain()
            context.dataStore.edit {
                it[viewerKey] = result.id
            }

            return Resource.Success(result)
        } catch (e: IOException) {
            Resource.Error(R.string.no_internet)
        } catch (e: JsonDataException) {
            Resource.Error(R.string.fail_parse)
        } catch (e: SQLiteException) {
            return (Resource.Error(R.string.sql_error))
        } catch (e: Exception) {
            e.printStackTrace()
            return (Resource.Error())
        }
    }

    suspend fun fetchUser(id: Int): Resource<User> {
        return try {
            val result = apiService.getUserById(id).toDomain()
            cachedUserDao.upsertUser(result.toEntity())
            context.dataStore.edit {
                it[cacheExpireKey(id)] = Instant.now().toEpochMilli()
            }

            return Resource.Success(result)
        } catch (e: IOException) {
            Resource.Error(R.string.no_internet)
        } catch (e: JsonDataException) {
            Resource.Error(R.string.fail_parse)
        } catch (e: SQLiteException) {
            return (Resource.Error(R.string.sql_error))
        } catch (e: Exception) {
            e.printStackTrace()
            return (Resource.Error())
        }
    }
}