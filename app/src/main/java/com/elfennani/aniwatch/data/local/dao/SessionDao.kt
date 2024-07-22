package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elfennani.aniwatch.data.local.entities.SessionEntity

@Dao
interface SessionDao {
    @Query("SELECT * FROM session WHERE id=:id")
    fun getSessionById(id: Long): SessionEntity

    @Insert
    suspend fun insertSession(sessionEntity: SessionEntity):Long
}