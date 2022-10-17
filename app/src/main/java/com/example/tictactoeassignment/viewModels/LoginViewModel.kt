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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
) : ViewModel() {
    var verificationId : String = ""
    var userName : String = ""
    var mobileNumber : String = ""

    private val authRepository : AuthRepository = AuthRepositoryImpl(Firebase.firestore)


    sealed class LoginScreenAction{
        object OnLogin : LoginScreenAction()
        object OnRegister : LoginScreenAction()
        object OnNewRegister : LoginScreenAction()
        object OnNewLogin : LoginScreenAction()
        object OnSuccessfulRegister : LoginScreenAction()
        object OnOtpSend : LoginScreenAction()
        object WrongOtp : LoginScreenAction()
    }

    enum class UserAuthState{
        OTP_SENT,USER_REGISTERED,LOGIN_STATE,NEW_REGISTER_STATE,REGISTER_USER_STATE,WRONG_OTP
    }

    data class LoginState(
        val userAuthState: UserAuthState = UserAuthState.LOGIN_STATE
    )

    private var _state : MutableStateFlow<LoginState> = MutableStateFlow(LoginState(UserAuthState.LOGIN_STATE))
    val state = _state.asStateFlow()

    private var _navigationUi : MutableSharedFlow<LoginState> = MutableSharedFlow()
    val navigationUi = _navigationUi.asSharedFlow()


    fun savePlayerData(player: Player){
        viewModelScope.launch(Dispatchers.IO) {
            playerRepository.savePlayerData(player)
        }
    }

    init {
        checkAndAutoSignInCurrentUser()
    }

    private fun checkAndAutoSignInCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentPlayerId = playerRepository.getCurrentPlayerId()
            authRepository.onLoginIn(currentPlayerId).onEach {player->
                if(player != null){
                    //To Game Screen
                    savePlayerData(player)
                    _navigationUi.emit(LoginState(UserAuthState.USER_REGISTERED))
                }
            }.launchIn(viewModelScope)
        }
    }


    fun onAction(loginScreenAction: LoginScreenAction){
        when(loginScreenAction){
            is LoginScreenAction.OnLogin -> {
                authRepository.onLoginIn(mobileNumber).onEach {player->
                    if(player != null){
                        //To Game Screen
                        savePlayerData(player)
                        _navigationUi.emit(LoginState(UserAuthState.USER_REGISTERED))
                    }
                }.launchIn(viewModelScope)
            }
            LoginScreenAction.OnOtpSend -> {
                _state.value = _state.value.copy(
                    userAuthState = UserAuthState.OTP_SENT
                )
            }
            is LoginScreenAction.OnSuccessfulRegister -> {
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
                _state.value = _state.value.copy(
                    userAuthState = UserAuthState.WRONG_OTP
                )
            }
            LoginScreenAction.OnNewRegister -> {
                _state.value = _state.value.copy(
                    userAuthState = UserAuthState.NEW_REGISTER_STATE
                )
            }
            LoginScreenAction.OnNewLogin -> {
                _state.value = _state.value.copy(
                    userAuthState = UserAuthState.LOGIN_STATE
                )
            }
            LoginScreenAction.OnRegister -> {
                _state.value = _state.value.copy(
                    userAuthState = UserAuthState.REGISTER_USER_STATE
                )
            }
        }
    }

    private fun createNewUser(player : Player){
        val db = Firebase.firestore
        db.collection("players").document(player.mobileNumber)
            .set(player)
            .addOnSuccessListener { documentReference ->
                _state.value = _state.value.copy(
                    userAuthState = UserAuthState.USER_REGISTERED
                )
            }
            .addOnFailureListener { e ->

            }
    }




}