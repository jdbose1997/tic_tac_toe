package com.example.tictactoeassignment.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GameRoom
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Random
import java.util.UUID
import javax.inject.Inject

@SuppressLint("MutableCollectionMutableState")
class GameRoomViewModel @Inject constructor(
): ViewModel() {
    private val gameRepository : GameRoomRepository = GameRoomRepositoryImpl(Firebase.firestore)
    var roomList : MutableStateFlow<List<GameRoom>> = MutableStateFlow(emptyList())


    init {
        fetchAllGameRooms("")
    }

    private fun fetchRoomList(){
        getUserAsModel().onEach {

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

    private fun fetchAllGameRooms(playerId : String){
        gameRepository.fetchGameRooms().onEach {gameRoomList->
            Log.i(TAG, "fetchAllGameRooms: ${gameRoomList.size}")
            roomList.emit(ArrayList(gameRoomList))
        }.launchIn(viewModelScope)
    }


     fun addPlayerToGameRoom(
        player: Player,roomId : String
    ){
        gameRepository.checkIfUserCanJoinRoom(roomId = roomId).onEach {
            if(it){
                gameRepository.fetchGameRoomUsingId(roomId).onEach {
                    it.apply {
                        if(!playerAllReadyJoined(players,player._id)){
                            currentPlayers += 1
                            players.add(player)
                            gameRepository.joinGameRoom(gameRoom = this,roomId).onEach {

                            }.launchIn(viewModelScope)
                        }
                    }
                }.launchIn(viewModelScope)
            }

        }.launchIn(viewModelScope)
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