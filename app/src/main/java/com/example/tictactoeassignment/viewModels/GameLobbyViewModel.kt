package com.example.tictactoeassignment.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GameSession
import com.example.data.model.repository.GameRepository
import com.example.domain.BoardCellValue
import com.example.domain.useCases.GetGameRoomUsingIdUseCase
import com.example.domain.useCases.InitNewGameBoardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameLobbyViewModel @Inject constructor(
    private val initNewGameBoardUseCase: InitNewGameBoardUseCase,
    private val getGameRoomUsingId: GetGameRoomUsingIdUseCase,
    private val gameRepository: GameRepository
) : ViewModel() {
    val gameStartTimer : MutableStateFlow<Int> = MutableStateFlow(5)
    private var _timer = 10
    var currentRoomId : String = ""
    var gameSessionId : String = ""

    private var firstPlayerId : String = ""
    private var secondPlayerId : String = ""



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
         getGameRoomUsingId(currentRoomId).onEach {
            val players = it.players
            if(players.size == 2){
                firstPlayerId = players.first()._id
                secondPlayerId = players.last()._id
                initGameSession()
            }
        }.launchIn(viewModelScope)
    }

    private fun initGameSession() {
        gameRepository.isAnyGameSessionExistWithTheSessionId(sessionId = gameSessionId).onEach {

            if(!it){
                val currentPlayerId =  if(Random.nextBoolean()) firstPlayerId else secondPlayerId
                initNewGameBoardUseCase(
                    gameSessionId, GameSession(
                        playerMoves = boardItemsReset,
                        currentTurn = BoardCellValue.CROSS.name,
                        lastPlayerId = currentPlayerId,
                        firstPlayerId = firstPlayerId,
                        secondPlayerId = secondPlayerId
                    )
                )
            }
        }.launchIn(viewModelScope)
    }
}