package com.example.data.model.repository

import com.example.data.model.GameSession
import com.example.data.model.RematchCall
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun createGamePlaySession(sessionId : String,gamePlayData : GameSession)
    fun updateGamePlayData(sessionId : String,boardMove : MutableMap<String,String>,currentMove : String,lastPlayerId : String,hasWon : Boolean)
    fun askForRematch(sessionId : String,rematchCall: RematchCall)
    fun acceptRematch(sessionId : String)
    fun deleteRematchRequest(sessionId : String)
    fun observeOtherPlayerMoves(gameSessionId : String): Flow<GameSession>
    fun observeRematch(gameSessionId : String): Flow<RematchCall>
    fun joinGame(gameSession: GameSession,sessionId: String,playerId : String)
}