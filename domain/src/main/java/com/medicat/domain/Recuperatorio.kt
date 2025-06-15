package com.medicat.domain

data class Recuperatorio(
    val id: Int,
    val completado: Boolean,
    val elementoCompetenciaId: Int,
    val fechaEvaluado: String?,
)
