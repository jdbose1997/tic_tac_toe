package com.example.domain.useCases

import com.example.data.model.repository.GameRepository
import com.example.data.model.repository.GameRoomRepository
import javax.inject.Inject

class DeleteGameSessionByIdUseCase  @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(sessionId : String) = gameRepository.deleteGameSessionById(sessionId = sessionId)
}