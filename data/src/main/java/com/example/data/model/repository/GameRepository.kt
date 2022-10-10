package com.example.data.model.repository

interface GameRepository {
    fun updateGamePlayData(gamePlayData : HashMap<Int, String>)
}