package com.example.data.model

data class Player(
    val _id : String ,
    val name : String,
    val mobileNumber : String,
    val isOnline : Boolean
){
    constructor() : this("","","",false)
}