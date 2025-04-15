package com.example.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MateriaResponseDto(
    val id: String,
    val elemComp: Int,
    val image: String,
    val recTotales: Int,
    val recTomados: Int,
    val elemEvaluados: Int,
    val description: String,
    val elemTotal: Int,
    val teacher: String,
) {
}
