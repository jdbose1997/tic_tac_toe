package com.example.data.model

data class GameSession(
    val playerMoves : MutableMap<String,String>,
    val currentTurn : String,
    val currentPlayerId : String
){
    constructor() : this(
        mutableMapOf(),"CROSS",""
    )
}