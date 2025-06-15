package com.medicat.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaberDto(
    @Json(name = "id") val id: Int,
    @Json(name = "completado") val completado: Boolean,
    @Json(name = "elemento_competencia_id") val elementoCompetenciaId: Int,
    @Json(name = "descripcion") val descripcion: String,
)
