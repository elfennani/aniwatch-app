package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elfennani.aniwatch.data.local.entities.WatchingShowsDto
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchingShowsDao {
    @Query("SELECT * FROM watching_shows")
    fun getShowsFlow(): Flow<List<WatchingShowsDto>>

    @Query("SELECT * FROM watching_shows")
    fun getShows(): List<WatchingShowsDto>

    @Query("DELETE FROM watching_shows")
    suspend fun deleteAll()

    @Insert
    fun insertAll(shows: List<WatchingShowsDto>)
}