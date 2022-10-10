package com.example.tictactoeassignment.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.data.model.GameRoom
import com.example.data.model.Player
import com.example.tictactoeassignment.viewModels.GameRoomViewModel
import com.example.tictactoeassignment.viewModels.LoginViewModel
import java.util.*

@Composable
fun GameRoomScreen(viewModel: GameRoomViewModel){

    val listOfGameRoom = viewModel.roomList.collectAsState().value
    LazyColumn(modifier = Modifier.fillMaxHeight().padding(12.dp)) {
        items(listOfGameRoom){gameRoom->
           RoomCard(gameRoom = gameRoom)
        }
    }
}

@Composable
fun RoomCard(gameRoom : GameRoom){
    Text(text = "Room Name : ${gameRoom.roomName}" )
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Players : ${gameRoom.currentPlayers}/2" )
    Spacer(modifier = Modifier.height(10.dp))
    Button(onClick = {

    }, modifier = Modifier.alpha(if(gameRoom.currentPlayers < 2) 1f else 0.5f), enabled = gameRoom.currentPlayers < 2) {
        Text("Join")
    }
    Spacer(modifier = Modifier.height(10.dp))
}