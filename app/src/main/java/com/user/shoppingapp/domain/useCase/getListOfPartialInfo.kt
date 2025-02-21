package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import com.user.shoppingapp.domain.models.Category
import javax.inject.Inject
class GetListOfPartialInfo @Inject constructor(private val repo:Repository){

    suspend fun getListOfPartialInfo(category: String)=repo.getProductByCategory(category)

}