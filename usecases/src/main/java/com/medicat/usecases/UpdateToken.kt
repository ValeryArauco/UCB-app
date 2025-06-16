package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.PushNotificationRepository

class UpdateToken(
    val pushNotificationRepository: PushNotificationRepository,
) {
    suspend fun invoke(
        email: String,
        token: String,
    ): NetworkResult<Boolean> = pushNotificationRepository.updateToken(email, token)
}
