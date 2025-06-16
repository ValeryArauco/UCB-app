package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.PushNotificationRepository

class DeleteNotification(
    val pushNotificationRepository: PushNotificationRepository,
) {
    suspend fun invoke(id: Int): NetworkResult<Boolean> = pushNotificationRepository.deleteNotificacion(id)
}
