package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.PushNotificationRepository

class MarkAsRead(
    val pushNotificationRepository: PushNotificationRepository,
) {
    suspend fun invoke(id: Int): NetworkResult<Boolean> = pushNotificationRepository.readNotificacion(id)
}
