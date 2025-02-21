package com.user.shoppingapp.presentation.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.user.shoppingapp.data.RepImpl.RepoImpl
import com.user.shoppingapp.domain.Repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module1 {
    @Provides
    @Singleton
    fun provideRepo(firestore: FirebaseFirestore,firebaseAuth: FirebaseAuth):Repository=RepoImpl(firestore,firebaseAuth)
}