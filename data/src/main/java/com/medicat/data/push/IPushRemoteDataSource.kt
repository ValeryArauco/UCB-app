package com.medicat.data.push

import com.medicat.data.NetworkResult
import com.medicat.domain.NotificacionItem

interface IPushRemoteDataSource {
    suspend fun fetchNotificaciones(email: String): NetworkResult<List<NotificacionItem>>

    suspend fun readNotificacion(id: Int): NetworkResult<Boolean>

    suspend fun deleteNotificacion(id: Int): NetworkResult<Boolean>

    suspend fun updateFcmToken(
        email: String,
        token: String,
    ): NetworkResult<Boolean>
}
