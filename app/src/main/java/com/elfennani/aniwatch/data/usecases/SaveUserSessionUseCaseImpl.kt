package com.elfennani.aniwatch.data.usecases

import android.database.sqlite.SQLiteException
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.entities.SessionEntity
import com.elfennani.aniwatch.domain.Resource
import com.elfennani.aniwatch.domain.usecases.SaveUserSessionUseCase

class SaveUserSessionUseCaseImpl(private val sessionDao: SessionDao) : SaveUserSessionUseCase {
    override suspend fun invoke(accessToken: String, expiration: Long): Resource<Long> {
        try {
            val sessionId = sessionDao.insertSession(SessionEntity(id = 0, token = accessToken, expiration=expiration))
            return Resource.Success(sessionId)
        } catch (e: SQLiteException) {
            return Resource.Error("SQL Error ${e.message}")
        } catch (e: Exception){
            return Resource.Error("Something wrong happened")
        }
    }
}