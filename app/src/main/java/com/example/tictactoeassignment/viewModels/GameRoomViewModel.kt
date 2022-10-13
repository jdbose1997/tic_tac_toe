package com.example.tictactoeassignment.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GameRoom
import com.example.data.model.Player
import com.example.domain.useCases.FetchGameRoomsUseCase
import com.example.domain.useCases.GetCurrentPlayerDataUseCase
import com.example.domain.useCases.JoinGameRoomUseCase
import com.example.tictactoeassignment.Constant.TAG
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class GameRoomViewModel @Inject constructor(
    private val getCurrentPlayerDataUseCase: GetCurrentPlayerDataUseCase,
    private val fetchGameRoomsUseCase: FetchGameRoomsUseCase,
    private val joinGameRoomUseCase: JoinGameRoomUseCase
): ViewModel() {

    var roomList : MutableStateFlow<List<GameRoom>> = MutableStateFlow(emptyList())
    private var player : Player ?= null

    init {
        fetchPlayerObject()
    }

    private fun fetchPlayerObject(){
        getCurrentPlayerDataUseCase().onEach {
            if(it != null){
                player = it
                fetchAllGameRooms(it._id)
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchAllGameRooms(playerId : String){
        fetchGameRoomsUseCase().onEach {gameRoomList->
            viewModelScope.launch {
                val job = async {
                    gameRoomList.forEach {
                        it.isTheCurrentUserAlredyJoined = playerAllReadyJoined(it.players,playerId)
                    }
                    gameRoomList
                }
                roomList.emit(ArrayList(job.await()))
            }

        }.launchIn(viewModelScope)
    }


     fun addPlayerToGameRoom(
        roomId : String
    ){
         player?.let { joinGameRoomUseCase.invoke(player = it, roomId = roomId,viewModelScope) }
    }


    fun createNewGameRoom(){
        val db = Firebase.firestore
        val roomId = UUID.randomUUID().toString()
        db.collection("game_room").document(roomId)
            .set(GameRoom(
                _roomId=roomId,
                roomName = "TEST ${Random().nextInt()}",
                currentPlayers = 0,
                players = arrayListOf()
            ))
            .addOnSuccessListener { documentReference ->
                 Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                 Log.w(TAG, "Error adding document", e)
            }
    }

    private fun playerAllReadyJoined(player : List<Player>,currentPlayerId : String) : Boolean{
        player.forEach {
            if(it._id == currentPlayerId) return true
        }
        return false
    }

}