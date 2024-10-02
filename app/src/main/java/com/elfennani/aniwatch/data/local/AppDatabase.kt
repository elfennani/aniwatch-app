package com.elfennani.aniwatch.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elfennani.aniwatch.data.local.dao.ActivityDao
import com.elfennani.aniwatch.data.local.dao.CharacterDao
import com.elfennani.aniwatch.data.local.dao.EpisodeDao
import com.elfennani.aniwatch.data.local.dao.RelationDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.dao.UserDao
import com.elfennani.aniwatch.data.local.models.LocalActivity
import com.elfennani.aniwatch.data.local.models.LocalCharacter
import com.elfennani.aniwatch.data.local.models.LocalEpisode
import com.elfennani.aniwatch.data.local.models.LocalMediaRelation
import com.elfennani.aniwatch.data.local.models.LocalSearch
import com.elfennani.aniwatch.data.local.models.LocalSession
import com.elfennani.aniwatch.data.local.models.LocalShow
import com.elfennani.aniwatch.data.local.models.LocalUser

@Database(
    version = 2,
    autoMigrations = [
        AutoMigration(1, 2)
    ],
    entities = [
        LocalSession::class,
        LocalActivity::class,
        LocalUser::class,
        LocalShow::class,
        LocalMediaRelation::class,
        LocalSearch::class,
        LocalCharacter::class,
        LocalEpisode::class
    ],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao
    abstract fun showDao(): ShowDao
    abstract fun relationDao(): RelationDao
    abstract fun characterDao(): CharacterDao
    abstract fun episodeDao(): EpisodeDao
}