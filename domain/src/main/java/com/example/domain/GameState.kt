package com.example.domain

import kotlin.random.Random

data class GameState(
    val playerCircleCount: Int = 0,
    val playerCrossCount: Int = 0,
    val drawCount: Int = 0,
    val hintText: String = "Player 'O' turn",
    val currentTurn: BoardCellValue = BoardCellValue.CIRCLE,
    val victoryType: VictoryType = VictoryType.NONE,
    val isCurrentPlayerMove : Boolean = false,
    val userInputs : MutableMap<String,String> = mutableMapOf(),
    val hasWon: Boolean = false,
    val hasGameDrawn : Boolean = false,
    val isRematchAsking : Boolean = false,
)

enum class BoardCellValue {
    CIRCLE,
    CROSS,
    NONE ;
    companion object{
        fun getRandomValue(): BoardCellValue {
            return if(Random.nextBoolean()) CROSS else CIRCLE
        }
    }
}

enum class VictoryType {
    HORIZONTAL1,
    HORIZONTAL2,
    HORIZONTAL3,
    VERTICAL1,
    VERTICAL2,
    VERTICAL3,
    DIAGONAL1,
    DIAGONAL2,
    NONE
}