package com.elfennani.aniwatch.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elfennani.aniwatch.data.local.dao.EpisodeDao
import com.elfennani.aniwatch.data.local.dao.ListingDao
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.dao.UserDao
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.local.dao.FeedDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.entities.ActivityEntity
import com.elfennani.aniwatch.data.local.entities.EpisodeEntity
import com.elfennani.aniwatch.data.local.entities.ListingItemEntity
import com.elfennani.aniwatch.data.local.entities.ShowEntity
import com.elfennani.aniwatch.data.local.entities.UserEntity
import com.elfennani.aniwatch.data.local.entities.DownloadedEpisodeEntity
import com.elfennani.aniwatch.data.local.entities.LocalEpisodeEntity
import com.elfennani.aniwatch.data.local.entities.SessionEntity

@Database(
    entities = [
        SessionEntity::class,
        ListingItemEntity::class,
        ShowEntity::class,
        EpisodeEntity::class,
        ActivityEntity::class,
        UserEntity::class,
        DownloadedEpisodeEntity::class,
        LocalEpisodeEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(1,2)
    ],
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun cachedListingDao(): ListingDao
    abstract fun cachedShowDao(): ShowDao
    abstract fun cachedEpisodesDao(): EpisodeDao
    abstract fun feedDao(): FeedDao
    abstract fun cachedUserDao(): UserDao
    abstract fun downloadDao(): DownloadDao
}