package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.Player

@Database(
    entities = [
        Player::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TicTacToeGameDatabase : RoomDatabase() {
    abstract fun playerDao() : PlayerDao
}