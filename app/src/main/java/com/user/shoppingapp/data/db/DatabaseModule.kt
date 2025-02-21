package com.user.shoppingapp.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context):FavouriteDatabase{
        return Room.databaseBuilder(
            context,
            FavouriteDatabase::class.java,
            "FavouriteDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(database: FavouriteDatabase):FavouriteDao=database.getDao()
}