package com.user.shoppingapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favourite(
    @PrimaryKey val id: String="",
    val name: String,
    val category: String,
    val imageUrl: String,
    val seller: String,
    val basePrice: String,
)
