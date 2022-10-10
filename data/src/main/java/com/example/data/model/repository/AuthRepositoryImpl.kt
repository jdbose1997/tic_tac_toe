package com.example.data.model.repository

import android.util.Log
import com.example.data.model.Player
import com.example.data.model.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val fireStoreCollection : FirebaseFirestore
) : AuthRepository{
    override fun onRegister(player: Player): Resource<Unit> {
        fireStoreCollection.collection("players").add(player).result
        return Resource.Error(Exception(""))
    }

    override fun onLoginIn(mobileNumber: String) : Flow<Player?> {
       return callbackFlow {
            fireStoreCollection.collection("players").document(mobileNumber).addSnapshotListener { value, error ->
                if(error != null){
                    trySend(null)
                    cancel()
                }

                if(value != null && value.exists()){
                    val player = value.toObject<Player>()
                    player?.let {
                        trySend(it)
                    } ?: kotlin.run {
                        trySend(null)
                    }
                }else{
                    trySend(null)
                }
                close()
            }
            awaitClose{
                close()
            }
        }
    //    Log.i("JAPAN", "onLoginIn: ${player?.name}")
    }


    override fun onLogout() {

    }

}