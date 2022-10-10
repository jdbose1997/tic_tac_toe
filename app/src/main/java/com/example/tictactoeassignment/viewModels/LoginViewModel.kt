package com.example.tictactoeassignment.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GameRoom
import com.example.data.model.Player
import com.example.data.model.repository.AuthRepository
import com.example.data.model.repository.AuthRepositoryImpl
import com.example.tictactoeassignment.Constant.TAG
import com.example.tictactoeassignment.navigation.Screen
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

class LoginViewModel : ViewModel() {

    private val authRepository : AuthRepository = AuthRepositoryImpl(Firebase.firestore)

    val navigateToUi : MutableSharedFlow<Screen> = MutableSharedFlow()

    sealed class LoginScreenAction{
        data class OnLogin(val mobileNumber : String,val userName : String) : LoginScreenAction()
        object OnOtpTyped : LoginScreenAction()
    }

    init {
        //createNewGameRoom()
    }

    fun onAction(loginScreenAction: LoginScreenAction){
        when(loginScreenAction){
            is LoginScreenAction.OnLogin -> {

                authRepository.onLoginIn(loginScreenAction.mobileNumber).onEach {player->
                    if(player != null){
                        //To Game Screen
                        Log.i(TAG, "onAction:navigate")
                        navigateToUi.emit(Screen.GameRoomScreen)
                    }else{
                        //Create New Player
                        createNewUser(
                            Player(
                                _id = loginScreenAction.mobileNumber,
                                name=loginScreenAction.userName,
                                mobileNumber = loginScreenAction.mobileNumber,
                                isOnline = true
                            )
                        )
                    }
                }.launchIn(viewModelScope)

            }
            is LoginScreenAction.OnOtpTyped ->{

            }
        }
    }

    fun createNewUser(player : Player){
        val db = Firebase.firestore
        db.collection("players").document(player.mobileNumber)
            .set(player)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun createNewGameRoom(){
        val db = Firebase.firestore
        db.collection("game_room").document("second_room")
            .set(GameRoom(
                _roomId = "12345",
                "TEST",
                0,
                arrayListOf()
            ))
            .addOnSuccessListener { documentReference ->
               // Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
              //  Log.w(TAG, "Error adding document", e)
            }
    }


}