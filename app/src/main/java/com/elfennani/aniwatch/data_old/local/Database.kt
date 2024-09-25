package com.elfennani.aniwatch.data_old.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elfennani.aniwatch.data_old.local.dao.CachedEpisodesDao
import com.elfennani.aniwatch.data_old.local.dao.CachedListingDao
import com.elfennani.aniwatch.data_old.local.dao.CachedShowDao
import com.elfennani.aniwatch.data_old.local.dao.CachedUserDao
import com.elfennani.aniwatch.data_old.local.dao.DownloadDao
import com.elfennani.aniwatch.data_old.local.dao.FeedDao
import com.elfennani.aniwatch.data_old.local.dao.SessionDao
import com.elfennani.aniwatch.data_old.local.entities.ActivityDto
import com.elfennani.aniwatch.data_old.local.entities.CachedEpisodeDto
import com.elfennani.aniwatch.data_old.local.entities.CachedListingDto
import com.elfennani.aniwatch.data_old.local.entities.CachedShowDto
import com.elfennani.aniwatch.data_old.local.entities.CachedUser
import com.elfennani.aniwatch.data_old.local.entities.LocalDownloadedEpisode
import com.elfennani.aniwatch.data_old.local.entities.SessionEntity

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
    version = 1,
    autoMigrations = [],
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
}