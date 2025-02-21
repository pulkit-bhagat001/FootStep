package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.data.RepImpl.RepoImpl
import com.user.shoppingapp.domain.Repository.Repository
import javax.inject.Inject

class AllCategoriesUseCase @Inject constructor(private val repo:Repository){
    suspend fun getAllCategories()=repo.getAllCategories()
}