package com.example.usecases

import com.example.data.MateriaRepository
import com.example.data.NetworkResult
import com.example.domain.Materia

class GetMaterias(
    val materiaRepository: MateriaRepository,
) {
    suspend fun invoke(email: String): NetworkResult<List<Materia>> = materiaRepository.fetchMateriasByDocente(email)
}
