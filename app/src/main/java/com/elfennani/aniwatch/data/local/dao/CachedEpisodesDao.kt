package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.elfennani.aniwatch.data.local.entities.CachedEpisodeDto
import com.elfennani.aniwatch.models.Episode

@Dao
interface CachedEpisodesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<CachedEpisodeDto>)
}