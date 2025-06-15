package com.medicat.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateRecuperatorioRequest(
    @Json(name = "completado") val completado: Boolean,
    @Json(name = "elemento_competencia_id") val elementoCompetenciaId: Int,
    @Json(name = "fecha_evaluado") val fechaEvaluado: String?,
)
