package com.example.data.model.repository

import com.example.data.model.GameSession
import java.util.concurrent.Flow

interface GameRepository {
    fun createGamePlaySession(sessionId : String,gamePlayData : GameSession)
    fun updateGamePlayData(sessionId : String,boardMove : MutableMap<String,String>,currentMove : String,lastPlayerId : String,hasWon : Boolean)
    fun observeOtherPlayerMoves(gameSessionId : String): kotlinx.coroutines.flow.Flow<GameSession>
    fun joinGame(gameSession: GameSession,sessionId: String,playerId : String)
}