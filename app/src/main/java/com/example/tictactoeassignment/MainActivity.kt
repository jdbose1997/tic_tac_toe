package com.example.tictactoeassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tictactoeassignment.navigation.SetupNavGraph
import com.example.tictactoeassignment.screens.GameScreen
import com.example.tictactoeassignment.screens.LoginScreen
import com.example.tictactoeassignment.ui.theme.TicTacToeAssignmentTheme
import com.example.tictactoeassignment.viewModels.GameRoomViewModel
import com.example.tictactoeassignment.viewModels.GameViewModel
import com.example.tictactoeassignment.viewModels.LoginViewModel
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
    private lateinit var navHostController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        setContent {
            TicTacToeAssignmentTheme {
                navHostController = rememberNavController()
                SetupNavGraph(navController = navHostController, loginViewModel = loginViewModel, gameRoomViewModel = gameRoomViewModel, gameViewModel = gameViewModel)
                //GameScreen(viewModel = gameViewModel)
                //LoginScreen(viewModel = loginViewModel, navController = navHostController)
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

