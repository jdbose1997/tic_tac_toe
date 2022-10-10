package com.example.tictactoeassignment.screens

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tictactoeassignment.viewModels.GameRoomViewModel

@Composable
fun GameRoomScreen(viewModel: GameRoomViewModel){

    LazyColumn(modifier = Modifier.fillMaxHeight()) {
     
        items(viewModel.roomList){player->
            Text(text = player.name)
        }
    }
}