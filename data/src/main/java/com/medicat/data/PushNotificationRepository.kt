package com.medicat.data

import com.medicat.data.push.IPushDataSource

class PushNotificationRepository(
    val push: IPushDataSource,
) {
    suspend fun getToken(): String = push.getToken()
}
