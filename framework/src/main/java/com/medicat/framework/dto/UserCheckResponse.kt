package com.medicat.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserCheckResponse(
    @Json(name = "exists") val exists: Boolean,
)
