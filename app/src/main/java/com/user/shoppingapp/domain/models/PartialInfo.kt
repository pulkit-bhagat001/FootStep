package com.user.shoppingapp.domain.models

import android.os.Parcelable
import kotlinx.serialization.Serializable


data class PartialInfo(
    val id:String="",
    val name:String="",
    val category:String="",
    val imageUrl: String="",
    val seller:String="",
    val basePrice:String="",
    var isFavourite:Boolean=false
)
