package com.example.data.model.repository

import com.example.data.model.Player
import com.example.data.model.Resource

interface AuthRepository {
    fun onRegister(player: Player) : Resource<Unit>
    fun onLogout()
}