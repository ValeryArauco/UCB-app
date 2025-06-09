package com.example.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateElementoRequest(
    @Json(name = "fecha_evaluado") val fechaEvaluado: String?,
    @Json(name = "fecha_registro") val fechaRegistro: String?,
    @Json(name = "saberes_completados") val saberesCompletados: Int,
    @Json(name = "completado") val completado: Boolean,
    @Json(name = "evaluado") val evaluado: Boolean,
    @Json(name = "comentario") val comentario: String?,
)
