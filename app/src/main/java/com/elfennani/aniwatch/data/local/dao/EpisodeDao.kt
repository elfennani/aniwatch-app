package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.entities.EpisodeEntity
import com.elfennani.aniwatch.data.local.entities.LocalEpisodeEntity

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeEntity>)

    @Query("DELETE FROM cached_episodes WHERE animeId=:animeId")
    suspend fun deleteByAnimeId(animeId: Int)

    @Query("DELETE FROM cached_episodes WHERE animeId=:showId AND id NOT IN (:epIds)")
    suspend fun deleteByShowIdAndIds(showId: Int, epIds: List<String>)

    @Upsert
    suspend fun upsertLocalEpisode(localEpisode: LocalEpisodeEntity)

    @Delete
    suspend fun deleteLocalEpisode(localEpisode: LocalEpisodeEntity)

    @Query("DELETE FROM local_episodes WHERE showId=:showId AND episode=:episode")
    suspend fun deleteLocalEpisode(showId: Int, episode: Double)
}