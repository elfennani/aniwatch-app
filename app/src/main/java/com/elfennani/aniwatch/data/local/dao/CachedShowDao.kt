package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elfennani.aniwatch.data.local.entities.CachedShowDto

@Dao
interface CachedShowDao{
    @Query("SELECT * FROM cached_shows WHERE id=:id")
    suspend fun getCachedShow(id: Int): CachedShowDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedShow(cachedShowDto: CachedShowDto)
}