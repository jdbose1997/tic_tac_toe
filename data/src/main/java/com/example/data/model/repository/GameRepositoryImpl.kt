package com.example.data.model.repository

import android.util.Log
import com.example.data.model.GameSession
import com.example.data.model.RematchCall
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random


class GameRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GameRepository {
    override fun createGamePlaySession(sessionId: String, gamePlayData: GameSession) {
        Log.i("JAPAN", "data: ${gamePlayData}")
        firestore.collection("game_session").document(sessionId).set(
            gamePlayData
        ).addOnSuccessListener {
        }.addOnCanceledListener {

        }.addOnFailureListener {
            Log.i("JAPAN", "addOnFailureListener: ${it}")
        }
    }

    override fun updateGamePlayData(
        sessionId: String,
        boardMove: MutableMap<String, String>,
        currentMove: String,
        lastPlayerId: String,
        hasWon : Boolean
    ) {
       try {
           firestore.collection("game_session").document(sessionId).update(
               mutableMapOf(
                   "playerMoves" to boardMove,
                   "currentTurn" to currentMove,
                   "lastPlayerId" to lastPlayerId,
                   "hasWon" to hasWon
               )
           ).addOnSuccessListener {
           }.addOnCanceledListener {

           }.addOnFailureListener {
               Log.i("JAPAN", "addOnFailureListener: ${it}")
           }
       }catch (e : Exception){
           e.printStackTrace()
       }
    }

    override fun askForRematch(
        sessionId: String,
        rematchCall: RematchCall
    ) {
        try {
            firestore.collection("game_session_rematch").document("rematch_$sessionId").set(
                rematchCall
            ).addOnSuccessListener {
                Log.i("JAPAN", "asked")
            }.addOnCanceledListener {

            }.addOnFailureListener {
                Log.i("JAPAN", "addOnFailureListener: ${it}")
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun acceptRematch(sessionId: String) {
        try {
            firestore.collection("game_session_rematch").document("rematch_$sessionId").update(
                mutableMapOf(
                    "requestAcceptedByOtherPlayer" to true,
                    "askingRematch" to false
                ) as Map<String, Boolean>
            ).addOnSuccessListener {
                Log.i("JAPAN", "accepted")
            }.addOnCanceledListener {

            }.addOnFailureListener {
                Log.i("JAPAN", "addOnFailureListener: ${it}")
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun deleteRematchRequest(sessionId: String) {
        try {
            firestore.collection("game_session_rematch").document("rematch_$sessionId").update(
                mutableMapOf(
                    "requestAcceptedByOtherPlayer" to false,
                    "askingRematch" to false
                ) as Map<String, Any>
            ).addOnSuccessListener {
                Log.i("JAPAN", "deleted")
            }.addOnCanceledListener {

            }.addOnFailureListener {
                Log.i("JAPAN", "addOnFailureListener: ${it}")
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    override fun observeOtherPlayerMoves(gameSessionId: String): Flow<GameSession> {
        return callbackFlow {
            try {
                firestore.collection("game_session").document(gameSessionId).addSnapshotListener { value, error ->
                    if(error != null){
                        cancel()
                    }

                    if(value != null && value.exists()){
                        val mappedBoard = value.toObject<GameSession>()
                        mappedBoard?.let {
                            trySend(it)
                        }
                    }
                }
            }catch (e : Exception){
                close()
            }
            awaitClose { close() }
        }
    }

    override fun observeRematch(gameSessionId: String): Flow<RematchCall> {
        return callbackFlow {
            try {
                firestore.collection("game_session_rematch").document("rematch_$gameSessionId").addSnapshotListener { value, error ->
                    if(error != null){
                        cancel()
                    }

                    if(value != null && value.exists()){
                        val mappedBoard = value.toObject<RematchCall>()
                        mappedBoard?.let {
                            trySend(it)
                        }
                    }
                }
            }catch (e : Exception){
                close()
            }
            awaitClose { close() }
        }
    }

    override fun joinGame(gameSession: GameSession, sessionId: String, playerId: String) {
        firestore.collection("game_session").document(sessionId).set(
            gameSession
        ).addOnSuccessListener {
        }.addOnCanceledListener {

        }.addOnFailureListener {
            Log.i("JAPAN", "addOnFailureListener: ${it}")
        }
    }

    override fun isAnyGameSessionExistWithTheSessionId(sessionId: String): Flow<Boolean> {
        return callbackFlow {
            try {
                firestore.collection("game_session").document(sessionId).addSnapshotListener { value, error ->
                    if(error != null){
                        cancel()
                    }
                    trySend(value != null && value.exists())
                }
            }catch (e : Exception){
                close()
            }
            awaitClose { close() }
        }
    }
}