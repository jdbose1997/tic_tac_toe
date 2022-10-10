package com.example.tictactoeassignment

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoeassignment.Constant.TAG
import com.example.tictactoeassignment.screens.GameBoard
import com.example.tictactoeassignment.screens.GameRoomScreen
import com.example.tictactoeassignment.screens.LoginScreen
import com.example.tictactoeassignment.ui.theme.TicTacToeAssignmentTheme
import com.example.tictactoeassignment.viewModels.GameRoomViewModel
import com.example.tictactoeassignment.viewModels.LoginViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val loginViewModel by viewModels<LoginViewModel>()
    private val gameRoomViewModel by viewModels<GameRoomViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        setContent {
            TicTacToeAssignmentTheme {

                GameRoomScreen(viewModel = gameRoomViewModel)
               // LoginScreen(viewModel = loginViewModel)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
//        if(currentUser != null){
//            setContent {
//                TicTacToeAssignmentTheme {
//                    val loginViewModel = viewModel<LoginViewModel>()
//                    LoginScreen(viewModel = loginViewModel)
//                }
//            }
//        }
    }
}

