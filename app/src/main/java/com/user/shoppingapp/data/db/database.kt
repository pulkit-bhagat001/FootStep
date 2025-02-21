package com.user.shoppingapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Favourite::class], version = 1)
abstract class FavouriteDatabase:RoomDatabase(){
    abstract fun getDao():FavouriteDao
}