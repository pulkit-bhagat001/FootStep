package com.user.shoppingapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.user.shoppingapp.common.Events
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteProduct(favourite: Favourite)

    @Query("Delete from Favourite where id=:pid")
    suspend fun deleteFavouriteProduct(pid:String)

    @Query("select id from Favourite")
    fun getUserId(): Flow<List<String>>

    @Query("Select * from Favourite")
    fun getAllFavouriteProducts():Flow<List<Favourite>>

    @Query("select count(id) from Favourite")
   fun getCount():Flow<Int>
}