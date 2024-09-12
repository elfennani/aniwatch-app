package com.elfennani.aniwatch.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.elfennani.aniwatch.data.local.dao.CachedEpisodesDao
import com.elfennani.aniwatch.data.local.dao.CachedListingDao
import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.dao.CachedUserDao
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.local.dao.FeedDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.entities.ActivityDto
import com.elfennani.aniwatch.data.local.entities.CachedEpisodeDto
import com.elfennani.aniwatch.data.local.entities.CachedListingDto
import com.elfennani.aniwatch.data.local.entities.CachedShowDto
import com.elfennani.aniwatch.data.local.entities.CachedUser
import com.elfennani.aniwatch.data.local.entities.LocalDownloadedEpisode
import com.elfennani.aniwatch.data.local.entities.SessionEntity

@Database(
    entities = [
        SessionEntity::class,
        CachedListingDto::class,
        CachedShowDto::class,
        CachedEpisodeDto::class,
        ActivityDto::class,
        CachedUser::class,
        LocalDownloadedEpisode::class
    ],
    version = 15,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
        AutoMigration(3, 4),
        AutoMigration(4, 5),
        AutoMigration(5, 6),
        AutoMigration(6, 7),
        AutoMigration(7, 8),
        AutoMigration(8, 9),
        AutoMigration(9, 10),
        AutoMigration(10, 11),
        AutoMigration(
            11,
            12,
            spec = com.elfennani.aniwatch.data.local.Database.WatchingToListingAutoMigration::class
        ),
        AutoMigration(12, 13),
        AutoMigration(
            13,
            14,
            spec = com.elfennani.aniwatch.data.local.Database.DownloadMigration::class
        ),
        AutoMigration(14, 15)
    ],
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun cachedListingDao(): CachedListingDao
    abstract fun cachedShowDao(): CachedShowDao
    abstract fun cachedEpisodesDao(): CachedEpisodesDao
    abstract fun feedDao(): FeedDao
    abstract fun cachedUserDao(): CachedUserDao
    abstract fun downloadDao(): DownloadDao

    @DeleteTable(tableName = "watching_shows")
    class WatchingToListingAutoMigration : AutoMigrationSpec

    @DeleteTable(tableName = "downloads")
    class DownloadMigration : AutoMigrationSpec
}