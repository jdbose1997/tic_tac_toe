package com.example.tictactoeassignment.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private var currentTurn = BoardCellValue.NONE.name
    var state by mutableStateOf(GameState())
    var myUserId : String = ""
    var gameSessionId : String = ""

    private val gameRepository : GameRepository = GameRepositoryImpl(
        firestore = Firebase.firestore
    )

    init {
        getUserData()
        viewModelScope.launch {
            delay(1000)
            gameReset()
        }
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









    fun onAction(action: PlayerAction) {
        when (action) {
            is PlayerAction.BoardTapped -> {
                boardItemsTest[action.cellNo] = currentTurn
                gameRepository.updateGamePlayData(
                    gameSessionId,
                    boardItemsTest,
                    currentTurn,
                    myUserId,
                    checkForVictory(currentTurn)

                )
            }
            PlayerAction.GameOver -> {
                gameReset()
            }
        }
    }

    private fun checkIfGameIsFinished(){
        if(checkForVictory(BoardCellValue.CIRCLE.name)){
            state = state.copy(
                hintText = "Player '${BoardCellValue.CIRCLE.name}' Won",
                currentTurn = BoardCellValue.NONE,
                hasWon = true
            )
        }else  if(checkForVictory(BoardCellValue.CROSS.name)){
            state = state.copy(
                hintText = "Player '${BoardCellValue.CROSS.name}' Won",
                currentTurn = BoardCellValue.NONE,
                hasWon = true
            )
        }else if(hasBoardFull()){
            state = state.copy(
                hintText = "Game Draw",
                drawCount = state.drawCount + 1
            )
        }
    }

    private fun gameReset() {
        gameRepository.updateGamePlayData(
            gameSessionId,
            boardItemsReset,
            currentTurn,
            myUserId,
            hasWon = false
        )
        state = state.copy(victoryType = VictoryType.NONE)
    }

    private fun checkForVictory(boardValue: String): Boolean {
        when {
            boardItemsTest["1"] == boardValue && boardItemsTest["2"] == boardValue && boardItemsTest["3"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
            }
            boardItemsTest["4"] == boardValue && boardItemsTest["5"] == boardValue && boardItemsTest["6"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }
            boardItemsTest["7"] == boardValue && boardItemsTest["8"] == boardValue && boardItemsTest["9"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }
            boardItemsTest["1"] == boardValue && boardItemsTest["4"] == boardValue && boardItemsTest["7"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }
            boardItemsTest["2"] == boardValue && boardItemsTest["5"] == boardValue && boardItemsTest["8"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }
            boardItemsTest["3"] == boardValue && boardItemsTest["6"] == boardValue && boardItemsTest["9"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }
            boardItemsTest["1"] == boardValue && boardItemsTest["5"] == boardValue && boardItemsTest["9"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }
            boardItemsTest["3"] == boardValue && boardItemsTest["5"] == boardValue && boardItemsTest["7"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL2)
                return true
            }
            else -> return false
        }
    }

    private fun hasBoardFull(): Boolean {
        if (boardItemsTest.containsValue(BoardCellValue.NONE.name)) return false
        return true
    }


     fun observeGameBoardMovements(){
        gameRepository.observeOtherPlayerMoves(gameSessionId).onEach {
            boardItemsTest = it.playerMoves
            checkIfGameIsFinished()
            currentTurn = it.currentTurn
            switchCurrentTurn()
            state = state.copy(hasWon = it.hasWon,isCurrentPlayerMove = it.lastPlayerId != myUserId, userInputs = it.playerMoves)
        }.launchIn(viewModelScope)
    }

    private fun switchCurrentTurn(){
        currentTurn = if(currentTurn == BoardCellValue.CROSS.name){
            BoardCellValue.CIRCLE.name
        }else{
            BoardCellValue.CROSS.name
        }
    }



}