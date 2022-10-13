package com.example.domain.useCases

import com.example.data.model.Player
import com.example.data.model.repository.GameRepository
import com.example.data.model.repository.GameRoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class JoinGameRoomUseCase @Inject constructor(
    private val gameRoomRepository: GameRoomRepository
) {

     operator fun invoke(player : Player,roomId : String,scope : CoroutineScope){
        gameRoomRepository.checkIfUserCanJoinRoom(roomId = roomId).onEach {
            if(it){
                gameRoomRepository.fetchGameRoomUsingId(roomId).onEach {
                    it.apply {
                        if(!playerAllReadyJoined(players, player._id)){
                            currentPlayers += 1
                            players.add(player!!)
                            gameRoomRepository.joinGameRoom(gameRoom = this,roomId).launchIn(scope)
                        }
                    }
                }.launchIn(scope)
            }

        }.launchIn(scope)
    }

    private fun playerAllReadyJoined(player : List<Player>,currentPlayerId : String) : Boolean{
        player.forEach {
            if(it._id == currentPlayerId) return true
        }
        return false
    }
}