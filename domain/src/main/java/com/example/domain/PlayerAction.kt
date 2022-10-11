package com.example.domain

sealed class PlayerAction {
    object GameOver: PlayerAction()
    data class BoardTapped(val cellNo: String): PlayerAction()
}
