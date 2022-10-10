package com.example.data.model.di

import com.example.data.model.repository.GameRoomRepository
import com.example.data.model.repository.GameRoomRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideGameRoomRepository(firestore: FirebaseFirestore) : GameRoomRepository = GameRoomRepositoryImpl(firestore)
}