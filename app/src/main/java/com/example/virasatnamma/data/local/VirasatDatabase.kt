package com.example.virasatnamma.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CheckInEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VirasatDatabase : RoomDatabase() {

    abstract fun checkInDao(): CheckInDao

    companion object {
        @Volatile
        private var INSTANCE: VirasatDatabase? = null

        fun getDatabase(context: Context): VirasatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VirasatDatabase::class.java,
                    "virasat_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
