package com.example.data.model.di

import android.content.Context
import androidx.room.Room
import com.example.data.model.repository.GameRoomRepository
import com.example.data.model.repository.GameRoomRepositoryImpl
import com.example.data.model.repository.PlayerRepository
import com.example.data.model.repository.PlayerRepositoryImpl
import com.example.db.PlayerDao
import com.example.db.TicTacToeGameDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): TicTacToeGameDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TicTacToeGameDatabase::class.java,
            "game_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providePlayerDao(database: TicTacToeGameDatabase) = database.playerDao()

    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideGameRoomRepository(firestore: FirebaseFirestore) : GameRoomRepository = GameRoomRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun providePlayerRepository(dao: PlayerDao) : PlayerRepository = PlayerRepositoryImpl(dao)
}