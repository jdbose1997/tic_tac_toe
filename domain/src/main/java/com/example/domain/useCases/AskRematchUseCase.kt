package com.example.domain.useCases

import com.example.data.model.RematchCall
import com.example.data.model.repository.GameRepository
import com.example.domain.BoardCellValue
import javax.inject.Inject

class AskRematchUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(sessionId : String,rematchCall: RematchCall){

        gameRepository.askForRematch(
            sessionId,
            rematchCall = rematchCall
        )
    }
}