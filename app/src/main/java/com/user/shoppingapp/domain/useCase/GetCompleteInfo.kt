package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import javax.inject.Inject

class GetCompleteInfo @Inject constructor(val repo:Repository) {
    suspend fun getCompleteInfo(id:String,size:String,color:String)=repo.getCompleteInfo(id,size,color)
}