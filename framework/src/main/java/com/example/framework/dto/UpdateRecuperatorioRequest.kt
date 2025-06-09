package com.example.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateRecuperatorioRequest(
    @Json(name = "completado") val completado: Boolean,
    @Json(name = "fecha_evaluado") val fechaEvaluado: String?,
)
