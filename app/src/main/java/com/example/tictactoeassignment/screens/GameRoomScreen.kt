package com.example.tictactoeassignment.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.data.model.GameRoom
import com.example.tictactoeassignment.navigation.Screen
import com.example.tictactoeassignment.viewModels.GameRoomViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun GameRoomScreen(navHostController: NavHostController,viewModel: GameRoomViewModel){
    val uiState by viewModel.state
    val context = LocalContext.current as Context

    val listOfGameRoom = viewModel.roomList.collectAsState().value
    when(uiState.uiActions){
        GameRoomViewModel.GameRoomScreenAction.OnCreateRoom -> {
            ShowCreateRoomAlertDialog(viewModel = viewModel)
        }
        else -> Unit
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)) {
        LazyColumn(modifier = Modifier
            .weight(1f)
            .fillMaxHeight()) {
            items(listOfGameRoom){gameRoom->
                RoomCard(navHostController=navHostController,gameRoom = gameRoom, viewModel = viewModel)
            }
        }

        Button(onClick = {
          viewModel.onAction(GameRoomViewModel.GameRoomScreenAction.OnCreateRoom)
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Create Room")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            viewModel.logOutCurrentUser()
            navHostController.navigate(Screen.LoginScreen.route)
            Toast.makeText(context,"Logged out!",Toast.LENGTH_SHORT).show()
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Log Out")
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
            navHostController.navigate("game_lobby_screen"+"/${gameRoom._roomId}")
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

@Composable
fun ShowCreateRoomAlertDialog(viewModel: GameRoomViewModel){
    val openDialog = remember { mutableStateOf(true) }
    var roomNameField by remember {
        mutableStateOf("")
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Create New Game Room")
            },
            text = {
                Column {
                    Text("Lets create a new game room")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Room Name")
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(value = roomNameField, onValueChange = {
                        roomNameField = it
                    })
                    Spacer(modifier = Modifier.height(10.dp))
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement =  Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .weight(1f),
                        onClick = {
                            viewModel.createNewGameRoom(roomNameField)
                        }
                    ) {
                        Text("Play")
                    }

                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .weight(1f),
                        onClick = {

                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}