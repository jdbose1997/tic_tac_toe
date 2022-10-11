package com.example.tictactoeassignment.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tictactoeassignment.navigation.Screen
import com.example.tictactoeassignment.viewModels.GameLobbyViewModel

@Composable
fun GameLobbyScreen(navHostController: NavHostController,viewModel : GameLobbyViewModel){

    val timerUi = viewModel.gameStartTimer.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
       Text(text = "Please wait")
       Spacer(modifier = Modifier.height(15.dp))
        Text(text = "The Game will start in ... ${timerUi}")
        Button(onClick = {
            viewModel.getPlayerDetailsFromGameRoomAndCreateGameSession()
            navHostController.navigate(Screen.GameScreen.route)}) {
            Text("Start")
        }
    }
}