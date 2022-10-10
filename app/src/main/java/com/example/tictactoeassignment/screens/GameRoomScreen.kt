package com.example.tictactoeassignment.screens

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.data.model.Player
import com.example.tictactoeassignment.viewModels.GameRoomViewModel
import com.example.tictactoeassignment.viewModels.LoginViewModel
import java.util.*

@Composable
fun GameRoomScreen(viewModel: GameRoomViewModel){

    val listOfPlayer = viewModel.roomList.collectAsState().value
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(listOfPlayer){player->
            Text(text = player.currentPlayers.toString())
        }
    }
}