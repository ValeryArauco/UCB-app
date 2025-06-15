package com.medicat.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ResponseDto<T>(
    @Json(name = "data")
    val data: List<T>,
)
