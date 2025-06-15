package com.medicat.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateMateriaRequest(
    @Json(name = "rec_tomados")
    val recTomados: Int,
    @Json(name = "elem_evaluados")
    val elemEvaluados: Int,
    @Json(name = "elem_completados")
    val elemCompletados: Int,
)
