package com.elfennani.aniwatch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.models.LocalSession

@Database(
    version = 1,
    entities = [LocalSession::class],
    autoMigrations = [],
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}