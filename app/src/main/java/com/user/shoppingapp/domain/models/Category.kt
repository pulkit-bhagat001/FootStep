package com.user.shoppingapp.domain.models

data class Category(
    var name:String="",
    var imageUri:String="",
    val date:Long=System.currentTimeMillis()
)
