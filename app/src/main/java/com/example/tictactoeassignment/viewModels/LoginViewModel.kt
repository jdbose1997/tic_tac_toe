package com.example.tictactoeassignment.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {
    var verificationId : String = ""
    var userName : String = ""
    var mobileNumber : String = ""

    private val authRepository : AuthRepository = AuthRepositoryImpl(Firebase.firestore)

    var navigateToUi  by mutableStateOf(Screen.SZero.route)

    sealed class LoginScreenAction{
        object OnLogin : LoginScreenAction()
        object OnRegister : LoginScreenAction()
        object OnOtpSend : LoginScreenAction()
        object WrongOtp : LoginScreenAction()
    }

    enum class UserAuthState{
        OTP_SENT,USER_REGISTERED,LOGIN_STATE,REGISTER_USER_STATE,WRONG_OTP
    }

    data class LoginState(
        val userAuthState: UserAuthState = UserAuthState.LOGIN_STATE
    )

    var state by mutableStateOf(LoginState())


    fun savePlayerData(player: Player){
        viewModelScope.launch(Dispatchers.IO) {
            playerRepository.savePlayerData(player)
        }
    }



    fun onAction(loginScreenAction: LoginScreenAction){
        when(loginScreenAction){
            is LoginScreenAction.OnLogin -> {
                authRepository.onLoginIn(mobileNumber).onEach {player->
                    if(player != null){
                        //To Game Screen
                        savePlayerData(player)
                        state = state.copy(
                            userAuthState = UserAuthState.USER_REGISTERED
                        )
                    }else{
                        state = state.copy(
                            userAuthState = UserAuthState.REGISTER_USER_STATE
                        )
                    }
                }.launchIn(viewModelScope)

            }
            LoginScreenAction.OnOtpSend -> {
                state = state.copy(
                    userAuthState = UserAuthState.OTP_SENT
                )
            }
            is LoginScreenAction.OnRegister -> {
                Log.i(TAG, "onAction: REGISTER ${mobileNumber}  ${userName}")
                createNewUser(
                    Player(
                        _id = mobileNumber,
                        name=userName,
                        mobileNumber = mobileNumber,
                        isOnline = true
                    )
                )
            }
            LoginScreenAction.WrongOtp -> {
                state = state.copy(
                    userAuthState = UserAuthState.WRONG_OTP
                )
            }
        }
    }

    fun createNewUser(player : Player){
        val db = Firebase.firestore
        db.collection("players").document(player.mobileNumber)
            .set(player)
            .addOnSuccessListener { documentReference ->
                state = state.copy(
                    userAuthState = UserAuthState.USER_REGISTERED
                )

            }
            .addOnFailureListener { e ->

            }
    }




}