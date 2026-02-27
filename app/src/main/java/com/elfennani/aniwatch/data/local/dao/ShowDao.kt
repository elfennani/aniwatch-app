package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.elfennani.aniwatch.data.local.entities.ShowEntity
import com.elfennani.aniwatch.data.local.relations.ShowWithEpisodes
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {
    @Transaction
    @Query("SELECT * FROM cached_shows WHERE id=:id")
    fun getCachedShow(id: Int): Flow<ShowWithEpisodes?>

    @Transaction
    @Query("SELECT * FROM cached_shows")
    fun getCachedShows(): Flow<List<ShowWithEpisodes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedShow(cachedShowDto: ShowEntity)
}