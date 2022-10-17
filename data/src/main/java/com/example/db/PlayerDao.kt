package com.example.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlayerData(player: Player)

    @Query("SELECT * FROM player")
    fun getPlayerData() : Flow<Player>

    @Query("SELECT * FROM player")
    fun getPlayerObject() : Player?

    @Query("DELETE FROM player")
    fun deleteCurrentPlayerData()
}