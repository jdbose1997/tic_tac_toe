package com.example.tictactoeassignment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tictactoeassignment.screens.GameLobbyScreen
import com.example.tictactoeassignment.screens.GameRoomScreen
import com.example.tictactoeassignment.screens.GameScreen
import com.example.tictactoeassignment.screens.LoginScreen
import com.example.tictactoeassignment.viewmodels.GameLobbyViewModel
import com.example.tictactoeassignment.viewmodels.GameRoomViewModel
import com.example.tictactoeassignment.viewmodels.GameViewModel
import com.example.tictactoeassignment.viewmodels.LoginViewModel


@Composable
fun SetupNavGraph(navController : NavHostController,
                  loginViewModel: LoginViewModel,
                  gameRoomViewModel: GameRoomViewModel,
                  gameViewModel: GameViewModel,
                  gameLobbyViewModel: GameLobbyViewModel){
        NavHost(navController = navController, startDestination =Screen.LoginScreen.route ){
            composable(
                route = Screen.LoginScreen.route
            ){
                LoginScreen(viewModel = loginViewModel,navController = navController)
            }

            composable(
                route = Screen.GameRoomScreen.route
            ){
                GameRoomScreen(viewModel = gameRoomViewModel, navHostController = navController)
            }

            composable(
                route = Screen.GameScreen.route,
                arguments = listOf(navArgument("game_session_id"){
                    type = NavType.StringType
                })
            ){
                val sessionId = it.arguments?.getString("game_session_id").toString()
                GameScreen(viewModel = gameViewModel, sessionId=sessionId)
            }

            composable(
                route = Screen.GameLobbyScreen.route,
                arguments = listOf(navArgument("room_id"){
                    type = NavType.StringType
                })
            ){
                val roomId = it.arguments?.getString("room_id").toString()
                GameLobbyScreen(viewModel = gameLobbyViewModel, navHostController = navController,roomId=roomId)
            }
        }
}