package com.example.agenda_kotlin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TareaEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TareaDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDao
    
    companion object {
        @Volatile
        private var INSTANCE: TareaDatabase? = null
        
        fun getDatabase(context: Context): TareaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TareaDatabase::class.java,
                    "tarea_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

