package com.example.data.model.repository

import com.example.data.model.Player
import com.example.data.model.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun onRegister(player: Player) : Resource<Unit>
    fun onLoginIn(mobileNumber : String) : Flow<Player?>
    fun onLogout()
}