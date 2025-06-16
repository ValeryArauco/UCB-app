package com.medicat.framework.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FcmTokenRequest(
    val email: String,
    val fcmToken: String,
)
