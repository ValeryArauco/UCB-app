package com.medicat.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FcmTokenRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "fcmToken")
    val fcmToken: String,
)
