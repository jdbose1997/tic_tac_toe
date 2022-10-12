package com.example.domain.useCases

import com.example.data.model.GameSession
import com.example.data.model.repository.GameRepository
import javax.inject.Inject

class InitNewGameBoardUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(sessionId : String,gameSession: GameSession) {
        gameRepository.createGamePlaySession(sessionId=sessionId, gamePlayData = gameSession)
    }
}