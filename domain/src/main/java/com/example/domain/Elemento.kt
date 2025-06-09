package com.example.domain

import kotlinx.serialization.Serializable

@Serializable
data class Elemento(
    val id: Int,
    val materiaId: String,
    val descripcion: String,
    val fechaLimite: String,
    val fechaRegistro: String?,
    val fechaEvaluado: String?,
    val saberesTotales: Int,
    val saberesCompletados: Int,
    val completado: Boolean,
    val evaluado: Boolean,
    val comentario: String?,
)
