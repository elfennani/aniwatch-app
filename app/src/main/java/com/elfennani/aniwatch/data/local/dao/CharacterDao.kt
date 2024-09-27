package com.elfennani.aniwatch.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.models.LocalCharacter

@Dao
interface CharacterDao {

    @Upsert
    suspend fun upsert(character: LocalCharacter)

    @Upsert
    suspend fun upsert(characters: List<LocalCharacter>)

    @Query("SELECT * FROM LocalCharacter WHERE showId=:showId")
    fun getPaging(showId: Int): PagingSource<Int, LocalCharacter>

    @Query("DELETE FROM LocalCharacter WHERE showId=:showId")
    suspend fun deleteByShowId(showId: Int)
}