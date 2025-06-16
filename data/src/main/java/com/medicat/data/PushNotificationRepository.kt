package com.medicat.data

import com.medicat.data.push.IPushDataSource
import com.medicat.data.push.IPushRemoteDataSource
import com.medicat.domain.NotificacionItem

class PushNotificationRepository(
    private val remoteDataSource: IPushRemoteDataSource,
    val push: IPushDataSource,
) {
    suspend fun getToken(): String = push.getToken()

    suspend fun getNotificaciones(email: String): NetworkResult<List<NotificacionItem>> = this.remoteDataSource.fetchNotificaciones(email)

    suspend fun readNotificacion(id: Int): NetworkResult<Boolean> = this.remoteDataSource.readNotificacion(id)

    suspend fun deleteNotificacion(id: Int): NetworkResult<Boolean> = this.remoteDataSource.deleteNotificacion(id)

    suspend fun updateToken(
        email: String,
        token: String,
    ): NetworkResult<Boolean> = this.remoteDataSource.updateFcmToken(email, token)
}
