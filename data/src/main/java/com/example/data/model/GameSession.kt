package com.example.data.model

data class GameSession(
    val playerMoves : MutableMap<String,String>,
    val currentTurn : String,
    val lastPlayerId : String,
    val firstPlayerId : String,
    val secondPlayerId : String,
    var hasWon : Boolean = false,
    var timer : Int = 30

){
    constructor() : this(
        mutableMapOf(),"CROSS","","",""
    )
}