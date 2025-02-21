package com.user.shoppingapp.domain.useCase

import com.user.shoppingapp.domain.Repository.Repository
import javax.inject.Inject

class GetBannerImages @Inject constructor(private val repo:Repository) {
   suspend fun getBannerImages()=repo.getBannerImages()
}