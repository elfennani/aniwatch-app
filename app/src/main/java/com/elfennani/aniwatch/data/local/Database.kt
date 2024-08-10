package com.elfennani.aniwatch.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.dao.CachedEpisodesDao
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.local.dao.FeedDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.local.entities.ActivityDto
import com.elfennani.aniwatch.data.local.entities.CachedEpisodeDto
import com.elfennani.aniwatch.data.local.entities.CachedShowDto
import com.elfennani.aniwatch.data.local.entities.DownloadDto
import com.elfennani.aniwatch.data.local.entities.SessionEntity
import com.elfennani.aniwatch.data.local.entities.WatchingShowsDto

@Database(
    entities = [
        SessionEntity::class,
        WatchingShowsDto::class,
        CachedShowDto::class,
        CachedEpisodeDto::class,
        ActivityDto::class,
        DownloadDto::class
    ],
    version = 6,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
        AutoMigration(3, 4),
        AutoMigration(4, 5),
        AutoMigration(5, 6)
    ],
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun watchingShowsDao(): WatchingShowsDao
    abstract fun cachedShowDao(): CachedShowDao
    abstract fun cachedEpisodesDao(): CachedEpisodesDao
    abstract fun feedDao(): FeedDao
    abstract fun downloadDao(): DownloadDao
}