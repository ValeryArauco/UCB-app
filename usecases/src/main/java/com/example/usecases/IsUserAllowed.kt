package com.example.usecases

import com.example.data.NetworkResult
import com.example.data.UserRepository

class ValidateUser(
    val userRepository: UserRepository,
) {
    suspend fun invoke(email: String): NetworkResult<Boolean> = userRepository.isUserAllowed(email)
}
