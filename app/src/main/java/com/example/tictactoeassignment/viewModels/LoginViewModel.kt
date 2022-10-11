package com.example.tictactoeassignment.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GameRoom
import com.example.data.model.Player
import com.example.data.model.repository.AuthRepository
import com.example.data.model.repository.AuthRepositoryImpl
import com.example.data.model.repository.PlayerRepository
import com.example.tictactoeassignment.Constant.TAG
import com.example.tictactoeassignment.navigation.Screen
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val authRepository : AuthRepository = AuthRepositoryImpl(Firebase.firestore)

    var navigateToUi  by mutableStateOf(Screen.SZero.route)

    sealed class LoginScreenAction{
        data class OnLogin(val mobileNumber : String,val userName : String) : LoginScreenAction()
        object OnOtpTyped : LoginScreenAction()
    }


    fun savePlayerData(player: Player){
        viewModelScope.launch(Dispatchers.IO) {
            playerRepository.savePlayerData(player)
        }
    }



    fun onAction(loginScreenAction: LoginScreenAction){
        when(loginScreenAction){
            is LoginScreenAction.OnLogin -> {
                authRepository.onLoginIn(loginScreenAction.mobileNumber).onEach {player->
                    if(player != null){
                        //To Game Screen
                        savePlayerData(player)
                        navigateToUi = Screen.GameRoomScreen.route
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




}