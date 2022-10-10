package com.example.tictactoeassignment.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn

class GameLobbyViewModel : ViewModel() {
    val gameStartTimer : MutableStateFlow<Int> = MutableStateFlow(5)
    private var _timer = 5

    init {
        flow<Int>{
           while (_timer >= 0){
               gameStartTimer.emit(_timer)
               _timer-=1
               delay(1000)
           }
        }.launchIn(viewModelScope)
    }
}