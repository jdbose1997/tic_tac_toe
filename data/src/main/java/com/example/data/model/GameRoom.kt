package com.example.data.model

data class GameRoom(
    val _roomId : String,
    val roomName : String,
    var currentPlayers : Int,
    val players : ArrayList<Player>,
    val createdBy : String,
    var isTheCurrentUserAlredyJoined : Boolean = false,
    var isCreatedByMe : Boolean = false
){
    constructor() : this(
        "",
        "",
        0,
        arrayListOf(),
        ""

    )
}