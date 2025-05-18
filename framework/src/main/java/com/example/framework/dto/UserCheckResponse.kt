package com.example.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserCheckRequest(
    @Json(name = "email") val email: String,
)

/**
 * Clase para la respuesta de verificación de usuario
 */
@JsonClass(generateAdapter = true)
data class UserCheckResponse(
    @Json(name = "exists") val exists: Boolean,
)
