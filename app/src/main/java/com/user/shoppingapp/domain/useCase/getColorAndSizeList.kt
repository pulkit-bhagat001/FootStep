package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import javax.inject.Inject

class GetColorAndSizeList @Inject constructor(private val repo:Repository) {
    suspend fun getColorAndSizeList(id:String)= repo.getColorAndSizeList(id)
}