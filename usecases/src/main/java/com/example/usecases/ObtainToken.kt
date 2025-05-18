package com.example.usecases

import com.example.data.PushNotificationRepository

class ObtainToken(
    val pushRepository: PushNotificationRepository,
) {
    suspend fun getToken(): String = this.pushRepository.getToken()
}
