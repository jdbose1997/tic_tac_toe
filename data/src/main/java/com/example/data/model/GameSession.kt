package com.example.data.model

data class GameSession(
    val playerMoves : MutableMap<String,String>,
    val currentTurn : String
){
    constructor() : this(
        mutableMapOf(),"CROSS"
    )
}