package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.models.LocalEpisode
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM LocalEpisode WHERE showId=:showId ORDER BY episode ASC")
    fun getListByShowId(showId: Int): Flow<List<LocalEpisode>>

    @Upsert
    suspend fun upsert(episode: LocalEpisode)

    @Upsert
    suspend fun upsert(episodes: List<LocalEpisode>)

    @Query("DELETE FROM LocalEpisode WHERE showId=:showId")
    suspend fun deleteByShowId(showId: Int)
}