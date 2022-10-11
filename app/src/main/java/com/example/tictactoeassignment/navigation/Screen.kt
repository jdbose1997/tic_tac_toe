package com.example.tictactoeassignment.navigation

sealed class Screen(val route : String = "") {
    object LoginScreen : Screen("login_screen")
    object GameRoomScreen : Screen("game_room_screen")
    object GameLobbyScreen : Screen("game_lobby_screen")
    object GameScreen : Screen("game_screen")
    object SZero : Screen()
}