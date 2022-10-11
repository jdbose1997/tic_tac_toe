package com.example.tictactoeassignment.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GameSession
import com.example.data.model.repository.GameRepository
import com.example.data.model.repository.GameRoomRepository
import com.example.domain.BoardCellValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameLobbyViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val gameRoomRepository: GameRoomRepository
) : ViewModel() {
    val gameStartTimer : MutableStateFlow<Int> = MutableStateFlow(5)
    private var _timer = 10
    var roomId : String = "second_room"

    private var firstPlayerId : String = ""
    private var secondPlayerId : String = ""
    private val gameSessionId : String = "test_game-715849400"


    var boardItemsReset: MutableMap<String, String> = mutableMapOf(
        "1" to BoardCellValue.NONE.name,
        "2" to BoardCellValue.NONE.name,
        "3" to BoardCellValue.NONE.name,
        "4" to BoardCellValue.NONE.name,
        "5" to BoardCellValue.NONE.name,
        "6" to BoardCellValue.NONE.name,
        "7" to BoardCellValue.NONE.name,
        "8" to BoardCellValue.NONE.name,
        "9" to BoardCellValue.NONE.name
    )



    private fun startGameTimer(){
        flow<Int>{
            while (_timer >= 0){
                gameStartTimer.emit(_timer)
                _timer-=1
                delay(1000)
            }
        }.launchIn(viewModelScope)
    }

     fun getPlayerDetailsFromGameRoomAndCreateGameSession(){
        gameRoomRepository.fetchGameRoomUsingId(roomId).onEach {
            val players = it.players
            if(players.size == 2){
                firstPlayerId = players.first()._id
                secondPlayerId = players.last()._id
                initGameSession()
            }else{
                Log.i("JAPAN", "getPlayerDetailsFromGameRoomAndCreateGameSession: less players")
            }
        }.launchIn(viewModelScope)
    }

    private fun initGameSession() {
        val currentPlayerId =  if(Random.nextBoolean()) firstPlayerId else secondPlayerId
        gameRepository.createGamePlaySession(
            gameSessionId, GameSession(
               playerMoves = boardItemsReset,
                currentTurn = BoardCellValue.CROSS.name,
                lastPlayerId = currentPlayerId,
                firstPlayerId = firstPlayerId,
                secondPlayerId = secondPlayerId
            )
        )
        Log.i("JAPAN", "ssession created")
        startGameTimer()
    }
}