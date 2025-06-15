package com.medicat.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MateriaDto(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "docente_id")
    val docenteId: Int,
    @Json(name = "paralelo")
    val paralelo: String,
    @Json(name = "elementos_totales")
    val elementosTotales: Int,
    @Json(name = "rec_totales")
    val recTotales: Int,
    @Json(name = "rec_tomados")
    val recTomados: Int,
    @Json(name = "elem_evaluados")
    val elemEvaluados: Int,
    @Json(name = "elem_completados")
    val elemCompletados: Int,
    @Json(name = "sigla")
    val sigla: String,
    @Json(name = "gestion")
    val gestion: String,
    @Json(name = "vigente")
    val vigente: Boolean,
)
