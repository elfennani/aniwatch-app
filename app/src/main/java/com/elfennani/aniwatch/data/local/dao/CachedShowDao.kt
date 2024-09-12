package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.elfennani.aniwatch.data.local.entities.CachedShowDto
import com.elfennani.aniwatch.data.local.entities.CachedShowWithEpisodes
import com.elfennani.aniwatch.models.ShowStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedShowDao {
    @Transaction
    @Query("SELECT * FROM cached_shows WHERE id=:id")
    fun getCachedShow(id: Int): Flow<CachedShowWithEpisodes?>

    @Transaction
    @Query("SELECT * FROM cached_shows")
    fun getCachedShows(): Flow<List<CachedShowWithEpisodes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedShow(cachedShowDto: CachedShowDto)
}