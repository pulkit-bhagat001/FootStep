package com.user.shoppingapp.common

import android.os.Parcelable
import com.user.shoppingapp.domain.models.PartialInfo
import kotlinx.serialization.Serializable

sealed class SubNavigation{
    @Serializable
    object LoginAndSignupScreen:SubNavigation()

    @Serializable
    object MainHomeScreen:SubNavigation()
}
sealed class Routes{
    @Serializable
    object LoginScreen

    @Serializable
    object SignUpScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object HomeScreen

    @Serializable
    object WishListScreen

    @Serializable
    object CartScreen

    @Serializable
    object CheckoutScreen

    @Serializable
    object PaymentScreen

    @Serializable
    object SeeAllProductScreen

    @Serializable
    object ProductDetailsScreen

    @Serializable
    object AllCategoryScreen

    @Serializable
    data class BuyNowScreen(
        val id:String="",
        val name:String="",
        val category:String="",
        val image:String="",
        val regPrice:String="",
        val finalPrice:String="",
        val noOfUnits:Int=0,
        val discountPercent:String="",
        val color:String="",
        val size:String=""
    )
}