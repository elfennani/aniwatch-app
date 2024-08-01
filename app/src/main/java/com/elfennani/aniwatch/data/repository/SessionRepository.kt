package com.elfennani.aniwatch.data.repository

import android.database.sqlite.SQLiteException
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.entities.SessionEntity
import com.elfennani.aniwatch.models.Resource


class SessionRepository(private val sessionDao: SessionDao) {
    suspend fun saveSession(accessToken: String, expiration: Long): Resource<Long> {
        try {
            val sessionId = sessionDao.insertSession(SessionEntity(id = 0, token = accessToken, expiration=expiration))
            return Resource.Success(sessionId)
        } catch (e: SQLiteException) {
            return Resource.Error(R.string.sql_error)
        } catch (e: Exception){
            return Resource.Error()
        }
    }
}