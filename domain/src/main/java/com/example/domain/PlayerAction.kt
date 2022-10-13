package com.example.domain

sealed class PlayerAction {
    object AskRematch: PlayerAction()
    object RematchAccept : PlayerAction()
    data class BoardTapped(val cellNo: String): PlayerAction()
}
