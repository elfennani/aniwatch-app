package com.elfennani.aniwatch.data_old.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elfennani.aniwatch.data_old.local.entities.CachedEpisodeDto

@Dao
interface CachedEpisodesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<CachedEpisodeDto>)

    @Query("DELETE FROM cached_episodes WHERE animeId=:animeId")
    suspend fun deleteByAnimeId(animeId: Int)

    @Query("DELETE FROM cached_episodes WHERE animeId=:showId AND id NOT IN (:epIds)")
    suspend fun deleteByShowIdAndIds(showId: Int, epIds: List<String>)
}