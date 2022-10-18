package com.example.tictactoeassignment.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GameRoom
import com.example.data.model.Player
import com.example.domain.useCases.*
import com.example.tictactoeassignment.Constant.TAG
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val joinGameRoomUseCase: JoinGameRoomUseCase,
    private val logoutCurrentUserUseCase: LogoutCurrentUserUseCase,
    private val deleteGameRoomByIdUseCase: DeleteGameRoomByIdUseCase,
    private val deleteGameSessionByIdUseCase: DeleteGameSessionByIdUseCase
): ViewModel() {
    private val _errorState : MutableStateFlow<String> = MutableStateFlow("")
    val errorState = _errorState.asStateFlow()


    val openDialog =  mutableStateOf(false)
    val openLogoutDialog =  mutableStateOf(false)

    private var _uiActions : MutableSharedFlow<GameRoomScreenAction> = MutableSharedFlow()
    val uiActions = _uiActions.asSharedFlow()





    sealed class GameRoomScreenAction{
        object OnCreateRoom : GameRoomScreenAction()
        object OnLogout : GameRoomScreenAction()
    }



    var roomList : MutableStateFlow<List<GameRoom>> = MutableStateFlow(emptyList())
    private var player : Player ?= null

    init {
        fetchPlayerObject()
    }

    fun onAction(gameRoomScreenAction: GameRoomScreenAction){
        viewModelScope.launch {
            _uiActions.emit(gameRoomScreenAction)
        }

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
                        it.isCreatedByMe = it.createdBy == playerId
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


    fun createNewGameRoom(roomName : String){
        if(roomName.isEmpty()){
            _errorState.value = "Please enter room name"
            return
        }
        val db = Firebase.firestore
        val roomId = UUID.randomUUID().toString()
        db.collection("game_room").document(roomId)
            .set(GameRoom(
                _roomId=roomId,
                roomName = roomName,
                currentPlayers = 0,
                players = arrayListOf(),
                createdBy = player?._id.toString()
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

    fun logOutCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            logoutCurrentUserUseCase()
        }
    }

    fun deleteGameRoom(roomId: String) {
        deleteGameRoomByIdUseCase(roomId=roomId).onEach {
            if(it){
                deleteGameSessionByIdUseCase(roomId).launchIn(viewModelScope)
            }
        }.launchIn(viewModelScope)
    }

}