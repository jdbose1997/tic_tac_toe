package com.example.data.model

data class GameSession(
    val playerMoves : MutableMap<String,String>,
    val currentTurn : String,
    val lastPlayerMoveId : String,
    var timer : Int = 30
){
    constructor() : this(
        mutableMapOf(),"CROSS",""
    )
}