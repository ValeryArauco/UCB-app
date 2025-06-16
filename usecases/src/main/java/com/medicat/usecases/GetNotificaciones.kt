package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.PushNotificationRepository
import com.medicat.domain.NotificacionItem

class GetNotificaciones(
    val pushNotificationRepository: PushNotificationRepository,
) {
    suspend fun invoke(email: String): NetworkResult<List<NotificacionItem>> = pushNotificationRepository.getNotificaciones(email)
}
