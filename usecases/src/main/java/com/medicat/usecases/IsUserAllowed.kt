package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.UserRepository

class IsUserAllowed(
    val userRepository: UserRepository,
) {
    suspend fun invoke(email: String): NetworkResult<Boolean> = userRepository.isUserAllowed(email)
}
