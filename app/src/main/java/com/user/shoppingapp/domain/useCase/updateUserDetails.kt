package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import com.user.shoppingapp.presentation.Viewmodel.ProfileScreenTextField
import javax.inject.Inject

class UpdateUserDetails @Inject constructor(private val repo: Repository) {
    suspend fun updateUserDetails(
        userid: String,
        info: ProfileScreenTextField
    ) = repo.updateUserDetails(userid, info)
}