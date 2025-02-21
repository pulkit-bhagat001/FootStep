package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import com.user.shoppingapp.presentation.Viewmodel.SignUpScreenTextField
import javax.inject.Inject

class AuthenticateUser @Inject constructor(private val repo:Repository) {
    suspend fun authenticateUser(userInfo: SignUpScreenTextField)=repo.authenticateUser(userInfo)
}