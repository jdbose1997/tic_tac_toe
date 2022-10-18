package com.example.data.model.repository

import android.util.Log
import com.example.data.model.GameRoom
import com.example.data.model.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GameRoomRepositoryImpl @Inject constructor(
    private val fireStoreCollection : FirebaseFirestore
)  : GameRoomRepository {

    override fun fetchGameRooms(): Flow<List<GameRoom>> {
        val gameRoomList = ArrayList<GameRoom>()
       return callbackFlow {
            fireStoreCollection.collection("game_room").addSnapshotListener { value, error ->
                gameRoomList.clear()
                if(error != null){
                    cancel(error.message.toString())
                }
                if(value != null &&  value.documents.isNotEmpty()){
                    value.documents.forEach {
                        val gameRooms = it.toObject(GameRoom::class.java)
                        gameRooms?.let {
                            gameRoomList.add(gameRooms)
                        }
                    }
                    trySend(gameRoomList)
                }
            }
           awaitClose{close()}
        }
    }

    override fun fetchGameRoomUsingId(roomdId: String): Flow<GameRoom> {
     return callbackFlow {
            fireStoreCollection.collection("game_room").document(roomdId).addSnapshotListener { value, error ->
                if(error != null){
                    close()
                }

                if(value != null && value.exists()){
                    val gameRoom = value.toObject<GameRoom>()
                    gameRoom?.let {
                        trySend(it)
                    } ?: kotlin.run {

                    }
                    close()
                }
            }
            awaitClose { close() }
        }
    }

    override fun joinGameRoom(gameRoom: GameRoom,roomId : String): Flow<Boolean> {
      return  callbackFlow {
            fireStoreCollection.collection("game_room")
                .document(roomId)
                .set(
                   gameRoom
                ).addOnSuccessListener {
                    trySend(true)
                    close()
                }.addOnCanceledListener {
                    trySend(false)
                    close()
                }
          awaitClose{
              close()
          }
        }
    }

    override fun deleteGameRoomById(roomId: String): Flow<Boolean> {
        return  callbackFlow {
            fireStoreCollection.collection("game_room")
                .document(roomId)
                .delete().addOnSuccessListener {
                    trySend(true)
                    close()
                }.addOnCanceledListener {
                    trySend(false)
                    close()
                }
            awaitClose{
                close()
            }
        }
    }


    override fun checkIfUserCanJoinRoom(roomId: String) : Flow<Boolean> {
       return callbackFlow {
            fireStoreCollection.collection("game_room").document(roomId).addSnapshotListener { value, error ->
                if(error != null){
                    trySend(false)
                    close()
                }
                if(value != null && value.exists()){
                    val roomObject = value.toObject(GameRoom::class.java)
                    val currentPlayer = roomObject?.currentPlayers ?: return@addSnapshotListener
                    if(currentPlayer < 2){
                        // add New Member
                        trySend(true)
                    }else{
                        trySend(false)
                    }
                    close()
                }
            }
           awaitClose { close() }
        }
    }



}