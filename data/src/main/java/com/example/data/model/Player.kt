package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class Player(
    @PrimaryKey(autoGenerate = false)
    val _id : String,
    val name : String,
    val mobileNumber : String,
    val isOnline : Boolean
){
    constructor() : this("","","",false)
}