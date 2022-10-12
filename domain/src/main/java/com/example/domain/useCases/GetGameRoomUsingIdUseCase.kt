package com.example.domain.useCases

import com.example.data.model.repository.GameRoomRepository
import javax.inject.Inject

class GetGameRoomUsingIdUseCase @Inject constructor(
    private val gameRoomRepository: GameRoomRepository
) {
    operator fun invoke(roomId : String) = gameRoomRepository.fetchGameRoomUsingId(roomId)
}