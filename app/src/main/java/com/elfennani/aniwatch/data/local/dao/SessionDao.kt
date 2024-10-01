package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.models.LocalSession

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: LocalSession):Long

    @Query("DELETE FROM LocalSession WHERE id=:id")
    suspend fun deleteSessionById(id:Int)

    @Query("SELECT * FROM LocalSession WHERE id=:id")
    suspend fun getSessionById(id: Int): LocalSession
}