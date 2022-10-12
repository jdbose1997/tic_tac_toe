package com.example.domain.useCases

import com.example.data.model.repository.GameRepository
import com.example.domain.BoardCellValue
import javax.inject.Inject

class UpdateCurrentGameBoardUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(gameBoardValues : MutableMap<String,String>,sessionId : String,currentTurn : String,lastPlayedUserId : String,hasWon : Boolean = false){

        gameRepository.updateGamePlayData(
            sessionId,
            gameBoardValues,
            currentMove = currentTurn,
            lastPlayerId = lastPlayedUserId,
            hasWon = hasWon
        )
    }
}