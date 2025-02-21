package com.user.shoppingapp.common

import com.google.firebase.auth.FirebaseUser

sealed class Events<out T> {
    object Loading:Events<Nothing>()
    data class Success<T>(var success:T):Events<T>()
    data class Error(val error:String):Events<Nothing>()
}
sealed class Events1<out T> {
    object Loading:Events1<Nothing>()
    data class Success(val success:Pair<FirebaseUser,String>):Events1<Pair<FirebaseUser,String>>()
    data class Error(val error:String):Events1<Nothing>()
}