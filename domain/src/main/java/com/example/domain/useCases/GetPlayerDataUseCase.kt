package com.example.domain.useCases

import com.example.data.model.repository.PlayerRepository
import javax.inject.Inject

class GetPlayerDataUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    operator fun invoke() = playerRepository.getPlayerData()
}