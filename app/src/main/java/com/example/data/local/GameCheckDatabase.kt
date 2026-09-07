package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserSpecsEntity::class, ReviewEntity::class, FavoriteGameEntity::class, PlayerProfileEntity::class, GameSubmissionEntity::class],
    version = 3,
    exportSchema = false
)
abstract class GameCheckDatabase : RoomDatabase() {
    abstract fun dao(): GameCheckDao

    companion object {
        @Volatile
        private var INSTANCE: GameCheckDatabase? = null

        fun getDatabase(context: Context): GameCheckDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameCheckDatabase::class.java,
                    "gamecheck_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
