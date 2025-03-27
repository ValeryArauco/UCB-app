package com.example.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SubjectResponseDto(
    @Json(name = "name")
    val id: String,

    @Json(name = "elemComp")
    val elemComp: Int,

    @Json(name = "image")
    val image: String,

    @Json(name = "elementosTotales")
    val elementosTotales: Int,

    @Json(name = "recTotales")
    val recTotales: Int,

    @Json(name = "recTomados")
    val recTomados: Int,

    @Json(name = "elemEvaluados")
    val elemEvaluados: Int,

    @Json(name = "description")
    val description: String,

    @Json(name = "elemTotal")
    val elemTotal: Int,

    @Json(name = "teacher")
    val teacher: String,

    @Json(name = "createTime")
    val createTime: String,

    @Json(name = "updateTime")
    val updateTime: String
) {
}
