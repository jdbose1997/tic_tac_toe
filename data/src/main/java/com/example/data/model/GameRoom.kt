package com.example.data.model

data class GameRoom(
    val _roomId : String,
    val roomName : String,
    var currentPlayers : Int,
    val players : ArrayList<Player>,
    var isTheCurrentUserAlredyJoined : Boolean = false
){
    constructor() : this(
        "",
        "",
        0,
        arrayListOf()
    )
}