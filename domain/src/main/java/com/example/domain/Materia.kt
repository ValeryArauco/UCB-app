package com.example.domain
import kotlinx.serialization.Serializable

@Serializable
data class Materia (
    val id: String,
    val name: String,
    val image: String,
    val docenteId: String,
    val elementosTotales: Int,
    val recTotales: Int,
    val recTomados: Int,
    val elemEvaluados: Int,
    val elemCompletados: Int
)