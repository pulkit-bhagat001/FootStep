package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import javax.inject.Inject

class GetNamesOfProducts @Inject constructor(private val repo:Repository){
    suspend fun getNamesOfProducts(query:String)=repo.getNamesOfProducts(query)
}