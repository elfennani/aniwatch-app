package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elfennani.aniwatch.data.local.entities.EpisodeEntity

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeEntity>)

    @Query("DELETE FROM cached_episodes WHERE animeId=:animeId")
    suspend fun deleteByAnimeId(animeId: Int)

    @Query("DELETE FROM cached_episodes WHERE animeId=:showId AND id NOT IN (:epIds)")
    suspend fun deleteByShowIdAndIds(showId: Int, epIds: List<String>)
}