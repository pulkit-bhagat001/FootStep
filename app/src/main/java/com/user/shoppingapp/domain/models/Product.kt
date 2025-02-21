package com.user.shoppingapp.domain.models

data class Product(
    val id:String="",
    val name:String="",
    val desc:String="",
    val category:String="",
    val imageUrl: String="",
    val seller:String="",
    val timestamp:Long=System.currentTimeMillis(),
    val basePrice:String="",
    val variant:VariantsInfo?=null,
    val isFavourite:Boolean=false

)
data class VariantsInfo(
    val regularPrice:String="",
    val finalPrice:String="",
    val discountPercentage:String="",
    val availableUnits:Int=0,
    val isAvailable:Boolean=true,

)
data class ColorAndSizeInfo(
    val color:List<String> = emptyList(),
    val size:List<String> = emptyList(),
)