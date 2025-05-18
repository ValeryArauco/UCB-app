package com.example.data

import com.example.data.push.IPushDataSource

class PushNotificationRepository(
    val push: IPushDataSource,
) {
    suspend fun getToken(): String = push.getToken()
}
