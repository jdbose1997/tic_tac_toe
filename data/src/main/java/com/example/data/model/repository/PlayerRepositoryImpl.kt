package com.example.data.model.repository

import com.example.data.model.Player
import com.example.db.PlayerDao
import com.example.db.TicTacToeGameDatabase
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao
) : PlayerRepository {
    override suspend fun savePlayerData(player: Player) {
        playerDao.addPlayerData(player)
    }

    override fun getPlayerData() = playerDao.getPlayerData()

}