package com.elfennani.aniwatch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elfennani.aniwatch.data.local.dao.ActivityDao
import com.elfennani.aniwatch.data.local.dao.RelationDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.dao.UserDao
import com.elfennani.aniwatch.data.local.models.LocalActivity
import com.elfennani.aniwatch.data.local.models.LocalMediaRelation
import com.elfennani.aniwatch.data.local.models.LocalSession
import com.elfennani.aniwatch.data.local.models.LocalShow
import com.elfennani.aniwatch.data.local.models.LocalUser

@Database(
    version = 1,
    entities = [
        LocalSession::class,
        LocalActivity::class,
        LocalUser::class,
        LocalShow::class,
        LocalMediaRelation::class
    ],
    autoMigrations = [],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao
    abstract fun showDao(): ShowDao
    abstract fun relationDao():RelationDao
}