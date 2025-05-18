package com.example.domain
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val nombre: String,
    val email: String,
    val photoUrl: String?,
    val activo: Boolean,
)
