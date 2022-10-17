package com.example.domain.useCases

import com.example.data.model.repository.PlayerRepository
import javax.inject.Inject

class LogoutCurrentUserUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {

    suspend operator fun invoke(){
        playerRepository.deleteCurrentPlayerData()
    }
}