package com.user.shoppingapp.domain.Repository

import com.google.firebase.auth.FirebaseUser
import com.user.shoppingapp.common.Events
import com.user.shoppingapp.common.Events1
import com.user.shoppingapp.domain.models.Category
import com.user.shoppingapp.domain.models.GetUserAddressAndPhoneNumber
import com.user.shoppingapp.domain.models.PartialInfo
import com.user.shoppingapp.domain.models.Product
import com.user.shoppingapp.presentation.Viewmodel.ProfileScreenTextField
import com.user.shoppingapp.presentation.Viewmodel.SignUpScreenTextField
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllCategories():Flow<Events<List<Category>>>
    suspend fun getProductByCategory(category:String): Flow<Events<List<PartialInfo>>>
    suspend fun getCompleteInfo(id:String,size:String,color:String):Flow<Events<Product>>
    suspend fun getColorAndSizeList(id:String):Flow<Events<Pair<List<String>,List<String>>>>
    suspend fun authenticateUser(userInfo: SignUpScreenTextField): Flow<Events1<Pair<FirebaseUser, String>>>
    suspend fun getProfileDetails(userId:String):Flow<Events<ProfileScreenTextField>>
    suspend fun updateUserDetails(userid:String,info:ProfileScreenTextField):Flow<Events<String>>
    suspend fun getUserAddressAndPhoneNumber():Flow<Events<GetUserAddressAndPhoneNumber>>
    suspend fun updateUserAddressAndPhoneNumber(address:String,phoneNumber:String):Flow<Events<String>>
    suspend fun getNamesOfProducts(query:String):Flow<Events<List<String>>>
    suspend fun getBannerImages():Flow<Events<List<String>>>

}