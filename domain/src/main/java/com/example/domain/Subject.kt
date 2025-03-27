package com.example.domain


data class Subject (
    val id: String,
    val name: String,
    val image: String,
    val teacherId: String,
    val elementosTotales: Int,
    val recTotales: Int,
    val recTomados: Int,
    val elemEvaluados: Int,
    val elemCompletes: Int
)