package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import javax.inject.Inject

class GetProfileDetails @Inject constructor(private val repo:Repository) {
    suspend fun getProfile(uuid:String)=repo.getProfileDetails(uuid)
}