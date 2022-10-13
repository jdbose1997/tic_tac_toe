package com.example.domain.useCases

import com.example.data.model.repository.GameRoomRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FetchGameRoomsUseCase @Inject constructor(
    private val gameRoomRepository: GameRoomRepository
) {


     operator fun invoke() = gameRoomRepository.fetchGameRooms()
}