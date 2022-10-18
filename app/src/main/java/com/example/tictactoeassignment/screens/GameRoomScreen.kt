package com.example.tictactoeassignment.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.data.model.GameRoom
import com.example.tictactoeassignment.navigation.Screen
import com.example.tictactoeassignment.viewmodels.GameRoomViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun GameRoomScreen(navHostController: NavHostController,viewModel: GameRoomViewModel){

    val context = LocalContext.current

    val listOfGameRoom = viewModel.roomList.collectAsState().value

    LaunchedEffect(key1 = Unit, block = {
        viewModel.uiActions.collect{actions->
            when(actions){
                GameRoomViewModel.GameRoomScreenAction.OnCreateRoom -> {
                    viewModel.openDialog.value = true
                }
                GameRoomViewModel.GameRoomScreenAction.OnLogout -> {
                   viewModel.openLogoutDialog.value = true
                }
            }
        }
    })


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
          viewModel.onAction(GameRoomViewModel.GameRoomScreenAction.OnLogout)
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Log Out")
        }
    }
    ShowCreateRoomAlertDialog(viewModel = viewModel)
    ShowDeleteRoomAlertDialog(viewModel = viewModel, context = context,navHostController=navHostController)
}

@Composable
fun RoomCard(navHostController: NavHostController,gameRoom : GameRoom,viewModel: GameRoomViewModel){
   Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
       Column {
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
       if(gameRoom.isCreatedByMe){
           IconButton(onClick = {
               viewModel.deleteGameRoom(gameRoom._roomId.toString())
           }) {
               Icon(
                   Icons.Filled.Delete,
                   "contentDescription",
               )
           }
       }
   }

}

@Composable
fun ShowCreateRoomAlertDialog(viewModel: GameRoomViewModel){

    val openDialog = remember {
        viewModel.openDialog
    }

    var roomNameField by remember {
        mutableStateOf("")
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                viewModel.openDialog.value = false
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
                            viewModel.openDialog.value = false
                            roomNameField = ""
                        }
                    ) {
                        Text("Create")
                    }

                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .weight(1f),
                        onClick = {
                            viewModel.openDialog.value = false
                            roomNameField = ""
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}


@Composable
fun ShowDeleteRoomAlertDialog(viewModel: GameRoomViewModel,context: Context,navHostController: NavHostController){

    val openDialog = remember {
        viewModel.openLogoutDialog
    }


    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                viewModel.openLogoutDialog.value = false
            },
            title = {
                Text(text = "Logout")
            },
            text = {
                Column {
                    Text("Do you want to logout?")
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
                            FirebaseAuth.getInstance().signOut()
                            viewModel.logOutCurrentUser()
                            navHostController.navigate(Screen.LoginScreen.route)
                            Toast.makeText(context,"Logged out!",Toast.LENGTH_SHORT).show()
                            viewModel.openLogoutDialog.value = false
                        }
                    ) {
                        Text("Logout")
                    }

                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .weight(1f),
                        onClick = {
                            viewModel.openLogoutDialog.value = false
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}