package com.example.framework.dto

import com.example.domain.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val success: Boolean,
    val user: User?,
    val message: String?
)
