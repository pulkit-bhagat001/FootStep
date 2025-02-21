package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import javax.inject.Inject

class UpdateUserAddressAndPhoneNumber @Inject constructor(private val repo:Repository) {
    suspend fun updatePhoneNumberAndAddress(address:String,phoneNumber:String)=repo.updateUserAddressAndPhoneNumber(address,phoneNumber)
}