package com.example.tictactoeassignment.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.data.model.GameRoom
import com.example.data.model.Player
import com.example.tictactoeassignment.Constant.TAG
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class LoginViewModel : ViewModel() {

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
//                createNewUser(
//                    Player(
//                        _id = loginScreenAction.mobileNumber,
//                        name=loginScreenAction.userName,
//                        mobileNumber = loginScreenAction.mobileNumber,
//                        isOnline = true
//                    )
//                )

            }
            is LoginScreenAction.OnOtpTyped ->{

            }
        }
    }

    fun createNewUser(player : Player){
        val db = Firebase.firestore
        db.collection("players")
            .add(player)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
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