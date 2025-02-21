package com.user.shoppingapp.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
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
    fun Firestore()=FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun firebaseAuth():FirebaseAuth=FirebaseAuth.getInstance()

}