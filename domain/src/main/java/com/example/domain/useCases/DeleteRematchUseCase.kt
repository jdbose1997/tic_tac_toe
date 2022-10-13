package com.example.domain.useCases

import com.example.data.model.repository.GameRepository
import javax.inject.Inject

class DeleteRematchUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    operator fun invoke(sessionId : String){
        gameRepository.deleteRematchRequest(sessionId)
    }
}