package com.example.tictactoeassignment.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GameSession
import com.example.data.model.repository.GameRepository
import com.example.data.model.repository.GameRepositoryImpl
import com.example.data.model.repository.PlayerRepository
import com.example.domain.BoardCellValue
import com.example.domain.GameState
import com.example.domain.PlayerAction
import com.example.domain.VictoryType
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private var currentTurn = BoardCellValue.NONE.name
    var state by mutableStateOf(GameState())
    var myUserId : String = ""
    var gameSessionId : String = "test_game-715849400"
    val userInputs : MutableSharedFlow<MutableMap<String,String>> = MutableSharedFlow()
    var isMyMove by mutableStateOf(false)
    private val gameRepository : GameRepository = GameRepositoryImpl(
        firestore = Firebase.firestore
    )

    init {
        getUserData()
    }

    private fun getUserData(){
        playerRepository.getPlayerData().onEach {
            try {
                myUserId = it._id
            }catch (e : Exception){
                e.stackTraceToString()
            }
        }.launchIn(viewModelScope)
    }

     var boardItemsTest: MutableMap<String, String> = mutableMapOf(
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

    init {
    //    initBoard()
        observeGameBoardMovements()
    }

    private fun initBoard(){
        gameRepository.updateGamePlayData(
            gameSessionId,
            GameSession(
                playerMoves = boardItemsReset,"X",""
            )
        )
    }

    var boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE,
    )





    fun onAction(action: PlayerAction) {
        when (action) {
            is PlayerAction.BoardTapped -> {
                boardItemsTest[action.cellNo] = currentTurn
                gameRepository.updateGamePlayData(
                    gameSessionId,
                    GameSession(
                        playerMoves = boardItemsTest,currentTurn,myUserId
                    )
                )
            }
            PlayerAction.GameOver -> {
                gameReset()
            }
        }
    }

    private fun gameReset() {
       gameRepository.updateGamePlayData(
           gameSessionId, GameSession(
               playerMoves = boardItemsReset,currentTurn,""
           )
       )
    }

    private fun addValueToBoard(cellNo: String) {
        if (boardItemsTest[cellNo] != BoardCellValue.NONE.name) {
            return
        }
        if (state.currentTurn == BoardCellValue.CIRCLE) {
            if (checkForVictory(BoardCellValue.CIRCLE)) {
                state = state.copy(
                    hintText = "Player 'O' Won",
                    playerCircleCount = state.playerCircleCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state = state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state = state.copy(
                    hintText = "Player 'X' turn",
                    currentTurn = BoardCellValue.CROSS
                )
            }
        } else if (state.currentTurn == BoardCellValue.CROSS) {
            if (checkForVictory(BoardCellValue.CROSS)) {
                state = state.copy(
                    hintText = "Player 'X' Won",
                    playerCrossCount = state.playerCrossCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state = state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state = state.copy(
                    hintText = "Player 'O' turn",
                    currentTurn = BoardCellValue.CIRCLE
                )
            }
        }
    }

    private fun checkForVictory(boardValue: BoardCellValue): Boolean {
        when {
            boardItems[1] == boardValue && boardItems[2] == boardValue && boardItems[3] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
            }
            boardItems[4] == boardValue && boardItems[5] == boardValue && boardItems[6] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }
            boardItems[7] == boardValue && boardItems[8] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[4] == boardValue && boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }
            boardItems[2] == boardValue && boardItems[5] == boardValue && boardItems[8] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }
            boardItems[3] == boardValue && boardItems[6] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[5] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }
            boardItems[3] == boardValue && boardItems[5] == boardValue && boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL2)
                return true
            }
            else -> return false
        }
    }

    private fun hasBoardFull(): Boolean {
        if (boardItems.containsValue(BoardCellValue.NONE)) return false
        return true
    }


    private fun observeGameBoardMovements(){
        gameRepository.observeOtherPlayerMoves(gameSessionId).onEach {
            currentTurn = it.currentTurn
            switchCurrentTurn()
            isMyMove = it.lastPlayerMoveId != myUserId
            boardItemsTest = it.playerMoves
            userInputs.emit(HashMap(it.playerMoves))
        }.launchIn(viewModelScope)
    }

    private fun switchCurrentTurn(){
        if(currentTurn == BoardCellValue.CROSS.name){
            currentTurn = BoardCellValue.CIRCLE.name
        }else{
            currentTurn = BoardCellValue.CROSS.name
        }
    }



}