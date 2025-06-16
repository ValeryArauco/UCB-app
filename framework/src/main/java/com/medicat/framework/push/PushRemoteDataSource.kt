package com.medicat.framework.push

import com.medicat.data.NetworkResult
import com.medicat.data.push.IPushRemoteDataSource
import com.medicat.domain.NotificacionItem
import com.medicat.framework.dto.FcmTokenRequest
import com.medicat.framework.mappers.toModel
import com.medicat.framework.service.RetrofitBuilder

class PushRemoteDataSource(
    val retrofitService: RetrofitBuilder,
) : IPushRemoteDataSource {
    override suspend fun fetchNotificaciones(email: String): NetworkResult<List<NotificacionItem>> {
        val response = retrofitService.apiService.getNotificaciones(email)
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()!!.data.map { it.toModel() })
        } else {
            return NetworkResult.Error(response.message())
        }
    }

    override suspend fun readNotificacion(id: Int): NetworkResult<Boolean> {
        val response = retrofitService.apiService.markAsRead(id)
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()?.exists ?: return NetworkResult.Error("Respuesta vacía del servidor"))
        } else {
            return NetworkResult.Error(response.message())
        }
    }

    override suspend fun deleteNotificacion(id: Int): NetworkResult<Boolean> {
        val response = retrofitService.apiService.deleteNotification(id)
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()?.exists ?: return NetworkResult.Error("Respuesta vacía del servidor"))
        } else {
            return NetworkResult.Error(response.message())
        }
    }

    override suspend fun updateFcmToken(
        email: String,
        token: String,
    ): NetworkResult<Boolean> {
        val request =
            FcmTokenRequest(
                email = email,
                fcmToken = token,
            )

        val response = retrofitService.apiService.updateFcmToken(request)

        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()?.exists ?: return NetworkResult.Error("Respuesta vacía del servidor"))
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}
