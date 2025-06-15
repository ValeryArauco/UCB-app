package com.medicat.domain

data class Saber(
    val id: Int,
    val completado: Boolean,
    val elementoCompetenciaId: Int,
    val descripcion: String,
)
