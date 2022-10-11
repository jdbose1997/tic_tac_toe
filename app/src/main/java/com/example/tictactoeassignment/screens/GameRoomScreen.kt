package com.example.tictactoeassignment.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.data.model.GameRoom
import com.example.data.model.Player
import com.example.tictactoeassignment.navigation.Screen
import com.example.tictactoeassignment.viewModels.GameRoomViewModel
import com.example.tictactoeassignment.viewModels.LoginViewModel
import java.util.*

@Composable
fun GameRoomScreen(navHostController: NavHostController,viewModel: GameRoomViewModel){

    val listOfGameRoom = viewModel.roomList.collectAsState().value
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        LazyColumn(modifier = Modifier
            .weight(1f)
            .fillMaxHeight()) {
            items(listOfGameRoom){gameRoom->
                RoomCard(navHostController=navHostController,gameRoom = gameRoom, viewModel = viewModel)
            }
        }

        Button(onClick = {
          viewModel.createNewGameRoom()
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Create Room")
        }
    }
}

@Composable
fun RoomCard(navHostController: NavHostController,gameRoom : GameRoom,viewModel: GameRoomViewModel){
    Text(text = "Room Name : ${gameRoom.roomName}" )
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Players : ${gameRoom.currentPlayers}/2" )
    Spacer(modifier = Modifier.height(10.dp))
    Button(onClick = {
        if(gameRoom.isTheCurrentUserAlredyJoined){
            navHostController.navigate(Screen.GameLobbyScreen.route)
        }else{
            viewModel.addPlayerToGameRoom(
                gameRoom._roomId
            )
        }
    }) {
        Text(if(gameRoom.isTheCurrentUserAlredyJoined) "Play" else "Join")
    }
    Spacer(modifier = Modifier.height(10.dp))
}