package com.medicat.usecases

import com.medicat.data.PushNotificationRepository

class ObtainToken(
    val pushRepository: PushNotificationRepository,
) {
    suspend fun getToken(): String = this.pushRepository.getToken()
}
