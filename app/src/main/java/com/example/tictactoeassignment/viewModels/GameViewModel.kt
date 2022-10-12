package com.example.tictactoeassignment.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.repository.PlayerRepository
import com.example.domain.BoardCellValue
import com.example.domain.GameState
import com.example.domain.PlayerAction
import com.example.domain.VictoryType
import com.example.domain.useCases.GetPlayerDataUseCase
import com.example.domain.useCases.UpdateCurrentGameBoardUseCase
import com.example.domain.useCases.ObserveGameBoardMovementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val updateCurrentGameBoardUseCase : UpdateCurrentGameBoardUseCase,
    private val observeGameBoardMovementsUseCase: ObserveGameBoardMovementsUseCase,
    private val getPlayerDataUseCase: GetPlayerDataUseCase
) : ViewModel() {
    private var currentTurn = BoardCellValue.NONE.name
    var state by mutableStateOf(GameState())
    var myUserId : String = ""
    var gameSessionId : String = ""



    init {
        state = state.copy(victoryType = VictoryType.NONE)
        getUserData()
    }

    private fun getUserData(){
        getPlayerDataUseCase().onEach {
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

    val initialBoardValue =  mutableMapOf(
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
                if(boardItemsTest[action.cellNo] == BoardCellValue.NONE.name){
                    boardItemsTest[action.cellNo] = currentTurn
                    updateCurrentGameBoardUseCase(
                        boardItemsTest,gameSessionId,currentTurn,myUserId,checkForVictory(boardItemsTest,currentTurn)
                    )
                }
            }
            PlayerAction.GameOver -> {
                gameReset()
            }
        }
    }

    private fun checkIfGameIsFinished(boardMoveItems : MutableMap<String,String>){
        if(checkForVictory(boardMoveItems,BoardCellValue.CIRCLE.name)){
            state = state.copy(
                hintText = "Player '${BoardCellValue.CIRCLE.name}' Won",
                currentTurn = BoardCellValue.NONE,
                hasWon = true
            )
        }else  if(checkForVictory(boardMoveItems,BoardCellValue.CROSS.name)){
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
        updateCurrentGameBoardUseCase(initialBoardValue,gameSessionId,currentTurn,myUserId)
    }

    private fun checkForVictory(boardMoveItems : MutableMap<String,String>,boardValue: String): Boolean {
        when {
            boardMoveItems["1"] == boardValue && boardMoveItems["2"] == boardValue && boardMoveItems["3"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
            }
            boardMoveItems["4"] == boardValue && boardMoveItems["5"] == boardValue && boardMoveItems["6"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }
            boardMoveItems["7"] == boardValue && boardMoveItems["8"] == boardValue && boardMoveItems["9"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }
            boardMoveItems["1"] == boardValue && boardMoveItems["4"] == boardValue && boardMoveItems["7"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }
            boardMoveItems["2"] == boardValue && boardMoveItems["5"] == boardValue && boardMoveItems["8"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }
            boardMoveItems["3"] == boardValue && boardMoveItems["6"] == boardValue && boardMoveItems["9"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }
            boardMoveItems["1"] == boardValue && boardMoveItems["5"] == boardValue && boardMoveItems["9"] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }
            boardMoveItems["3"] == boardValue && boardMoveItems["5"] == boardValue && boardMoveItems["7"] == boardValue -> {
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
         observeGameBoardMovementsUseCase(gameSessionId).onEach {
            boardItemsTest = it.playerMoves
            checkIfGameIsFinished(it.playerMoves)
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