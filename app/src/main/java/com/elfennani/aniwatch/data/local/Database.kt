package com.elfennani.aniwatch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.entities.SessionEntity

@Database(entities = [SessionEntity::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}