package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.Player

@Database(entities = [Player::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao() : PlayerDao
}