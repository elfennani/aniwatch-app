package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.elfennani.aniwatch.data.local.entities.CachedShowDto
import com.elfennani.aniwatch.data.local.entities.CachedShowWithEpisodes

@Dao
interface CachedShowDao{
    @Transaction
    @Query("SELECT * FROM cached_shows WHERE id=:id")
    suspend fun getCachedShow(id: Int): CachedShowWithEpisodes?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedShow(cachedShowDto: CachedShowDto)
}