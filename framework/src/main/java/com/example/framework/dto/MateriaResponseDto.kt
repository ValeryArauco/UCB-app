package com.example.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MateriaResponseDto(
    @Json(name = "documents")
    val documents: List<MateriaDto>
) {
}
