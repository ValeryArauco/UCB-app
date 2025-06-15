package com.medicat.usecases

import com.medicat.data.MateriaRepository
import com.medicat.data.NetworkResult
import com.medicat.domain.Materia

class GetMaterias(
    val materiaRepository: MateriaRepository,
) {
    suspend fun invoke(email: String): NetworkResult<List<Materia>> = materiaRepository.fetchMateriasByDocente(email)
}
