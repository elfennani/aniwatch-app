package com.elfennani.aniwatch.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.dao.CachedEpisodesDao
import com.elfennani.aniwatch.data.local.dao.FeedDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.local.entities.ActivityDto
import com.elfennani.aniwatch.data.local.entities.CachedEpisodeDto
import com.elfennani.aniwatch.data.local.entities.CachedShowDto
import com.elfennani.aniwatch.data.local.entities.SessionEntity
import com.elfennani.aniwatch.data.local.entities.WatchingShowsDto

@Database(
    entities = [
        SessionEntity::class,
        WatchingShowsDto::class,
        CachedShowDto::class,
        CachedEpisodeDto::class,
        ActivityDto::class
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ],
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun watchingShowsDao(): WatchingShowsDao
    abstract fun cachedShowDao(): CachedShowDao
    abstract fun cachedEpisodesDao(): CachedEpisodesDao
    abstract fun feedDao(): FeedDao
}