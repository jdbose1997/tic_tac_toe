package com.example.tictactoeassignment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tictactoeassignment.screens.GameLobbyScreen
import com.example.tictactoeassignment.screens.GameRoomScreen
import com.example.tictactoeassignment.screens.GameScreen
import com.example.tictactoeassignment.screens.LoginScreen
import com.example.tictactoeassignment.viewModels.GameLobbyViewModel
import com.example.tictactoeassignment.viewModels.GameRoomViewModel
import com.example.tictactoeassignment.viewModels.GameViewModel
import com.example.tictactoeassignment.viewModels.LoginViewModel


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
                route = Screen.GameScreen.route
            ){
                GameScreen(viewModel = gameViewModel)
            }

            composable(
                route = Screen.GameLobbyScreen.route
            ){
                GameLobbyScreen(viewModel = gameLobbyViewModel)
            }
        }
}