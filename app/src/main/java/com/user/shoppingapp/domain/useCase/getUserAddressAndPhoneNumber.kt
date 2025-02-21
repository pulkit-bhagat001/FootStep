package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import javax.inject.Inject

class GetUserAddressAndPhoneNumber @Inject constructor(private val repo:Repository) {
    suspend fun getDetails()=repo.getUserAddressAndPhoneNumber()
}