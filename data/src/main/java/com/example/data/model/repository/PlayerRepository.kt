package com.example.data.model.repository

import com.example.data.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun savePlayerData(player: Player)
    fun getPlayerData() : Flow<Player>
}