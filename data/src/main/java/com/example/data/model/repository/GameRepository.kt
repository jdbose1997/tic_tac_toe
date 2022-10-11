package com.example.data.model.repository

import com.example.data.model.GameSession
import java.util.concurrent.Flow

interface GameRepository {
    fun updateGamePlayData(sessionId : String,gamePlayData : GameSession)
    fun observeOtherPlayerMoves(gameSessionId : String): kotlinx.coroutines.flow.Flow<GameSession>
}