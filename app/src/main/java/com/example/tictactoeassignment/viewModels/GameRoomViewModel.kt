package com.example.tictactoeassignment.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Player
import com.example.data.model.repository.GameRoomRepository
import com.example.data.model.repository.GameRoomRepositoryImpl
import com.example.tictactoeassignment.Constant.TAG
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MutableCollectionMutableState")
class GameRoomViewModel @Inject constructor(

): ViewModel() {
    private val gameRepository : GameRoomRepository = GameRoomRepositoryImpl(Firebase.firestore)
    var roomList by mutableStateOf(ArrayList<Player>())

    init {
        fetchRoomList()
        addPlayerToGameRoom(
            Player(
                "s",
                "6667777",
                "asasasasa",
                false
            ),
"123456"
        )
    }

    private fun fetchRoomList(){
        getUserAsModel().onEach {
            Log.i(TAG, "DATA: ${it}")
            roomList.apply {
                clear()
                addAll(it)
            }
        }.launchIn(viewModelScope)
    }

    private fun getUserAsModel(): Flow<List<Player>> {
        val playerList = ArrayList<Player>()
        return callbackFlow {
            val db = Firebase.firestore
            val listRef = db.collection("players")

            listRef.addSnapshotListener { value, error ->
                playerList.clear()
                if(error != null){
                    cancel(error.message.toString())
                }
                if(value != null &&  value.documents.isNotEmpty()){
                    value.documents.forEach {
                        val player = it.toObject(Player::class.java)
                        player?.let {
                            playerList.add(player)
                        }
                    }
                    trySend(playerList)
                }
            }
            awaitClose { close() }
        }

    }


    private fun addPlayerToGameRoom(
        player: Player,roomId : String
    ){
        gameRepository.checkIfUserCanJoinRoom(roomId = roomId).onEach {
          if(it){
              gameRepository.fetchGameRoomUsingId(roomdId = roomId).onEach {gameRoom->
                  gameRoom.apply {
                      currentPlayers += 1
                      players.add(player)
                  }
                  gameRepository.joinGameRoom(gameRoom=gameRoom, player = player).onEach {isSaved->
                  }.launchIn(viewModelScope)
              }.launchIn(viewModelScope)
          }
        }.launchIn(viewModelScope)
    }


}