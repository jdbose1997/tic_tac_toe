package com.example.data.model.repository

import com.example.data.model.GameRoom
import com.example.data.model.Player
import com.example.data.model.Resource
import kotlinx.coroutines.flow.Flow

interface GameRoomRepository {
    fun fetchGameRooms() : Flow<List<GameRoom>>
    fun fetchGameRoomUsingId(roomdId : String) : Flow<GameRoom>
    fun checkIfUserCanJoinRoom(roomId: String) : Flow<Boolean>
    fun joinGameRoom(gameRoom: GameRoom,player: Player): Flow<Boolean>
}