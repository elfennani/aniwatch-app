package com.elfennani.aniwatch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.local.entities.SessionEntity
import com.elfennani.aniwatch.data.local.entities.WatchingShowsDto

@Database(entities = [SessionEntity::class, WatchingShowsDto::class], version = 3)
@TypeConverters(Converters::class)
abstract class Database: RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun watchingShowsDao(): WatchingShowsDao
}