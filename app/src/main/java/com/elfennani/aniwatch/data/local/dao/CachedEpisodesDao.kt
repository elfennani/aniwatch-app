package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elfennani.aniwatch.data.local.entities.CachedEpisodeDto
import com.elfennani.aniwatch.models.Episode

@Dao
interface CachedEpisodesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<CachedEpisodeDto>)

    @Query("DELETE FROM cached_episodes WHERE animeId=:animeId")
    suspend fun deleteByAnimeId(animeId: Int)
}