package com.example.data.model.repository

import com.example.data.model.Player
import com.example.data.model.Resource
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val fireStoreCollection : FirebaseFirestore
) : AuthRepository{
    override fun onRegister(player: Player): Resource<Unit> {
        fireStoreCollection.collection("players").add(player).result
        return Resource.Error(Exception(""))
    }


    override fun onLogout() {

    }

}