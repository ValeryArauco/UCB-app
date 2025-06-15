package com.medicat.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ElementoDto(
    @Json(name = "id") val id: Int,
    @Json(name = "materia_id") val materiaId: String,
    @Json(name = "descripcion") val descripcion: String,
    @Json(name = "fecha_limite") val fechaLimite: String,
    @Json(name = "fecha_evaluado") val fechaEvaluado: String?,
    @Json(name = "fecha_registro") val fechaRegistro: String?,
    @Json(name = "saberes_totales") val saberesTotales: Int,
    @Json(name = "saberes_completados") val saberesCompletados: Int,
    @Json(name = "completado") val completado: Boolean,
    @Json(name = "evaluado") val evaluado: Boolean,
    @Json(name = "comentario") val comentario: String,
)
