package com.example.tictactoeassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tictactoeassignment.navigation.SetupNavGraph
import com.example.tictactoeassignment.ui.theme.TicTacToeAssignmentTheme
import com.example.tictactoeassignment.viewmodels.GameLobbyViewModel
import com.example.tictactoeassignment.viewmodels.GameRoomViewModel
import com.example.tictactoeassignment.viewmodels.GameViewModel
import com.example.tictactoeassignment.viewmodels.LoginViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val loginViewModel : LoginViewModel by viewModels()
    private val gameRoomViewModel by viewModels<GameRoomViewModel>()
    private val gameViewModel by viewModels<GameViewModel>()
    private val gameLobbyViewModel by viewModels<GameLobbyViewModel>()
    private lateinit var navHostController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        setContent {
            TicTacToeAssignmentTheme {
                navHostController = rememberNavController()
                SetupNavGraph(navController = navHostController, loginViewModel = loginViewModel, gameRoomViewModel = gameRoomViewModel, gameViewModel = gameViewModel, gameLobbyViewModel = gameLobbyViewModel)
            }
        }
    }
}


