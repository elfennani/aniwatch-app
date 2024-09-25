package com.elfennani.aniwatch.data_old.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elfennani.aniwatch.data_old.local.entities.SessionEntity

@Dao
interface SessionDao {
    @Query("SELECT * FROM session WHERE id=:id")
    fun getSessionById(id: Long): SessionEntity?

    @Insert
    suspend fun insertSession(sessionEntity: SessionEntity):Long
}