package com.medicat.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificacionDto(
    @Json(name = "id") val id: Int,
    @Json(name = "usuario_id") val userId: Int,
    @Json(name = "competencia_id") val competitionId: Int,
    @Json(name = "tipo") val type: String,
    @Json(name = "titulo") val title: String,
    @Json(name = "mensaje") val message: String,
    @Json(name = "leida") val isRead: Boolean,
    @Json(name = "fecha") val sentAt: String,
)
