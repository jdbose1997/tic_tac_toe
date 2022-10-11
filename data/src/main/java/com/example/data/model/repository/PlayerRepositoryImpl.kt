package com.example.data.model.repository

import com.example.data.model.Player
import com.example.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val db : AppDatabase
) : PlayerRepository {
    override suspend fun savePlayerData(player: Player) {
        db.playerDao().addPlayerData(player)
    }

    override fun getPlayerData() = db.playerDao().getPlayerData()

}