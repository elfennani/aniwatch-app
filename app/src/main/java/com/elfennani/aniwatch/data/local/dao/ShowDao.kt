package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.models.LocalShow

@Dao
interface ShowDao {
    @Upsert
    suspend fun upsert(show: LocalShow)

    @Upsert
    suspend fun upsert(shows:List<LocalShow>)

    @Query("SELECT * FROM LocalShow WHERE id=:id")
    suspend fun getById(id:Int): LocalShow?
}