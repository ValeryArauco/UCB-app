package com.medicat.data

import com.medicat.data.registrarAvance.IMateriaRemoteDataSource
import com.medicat.domain.Materia

class MateriaRepository(
    private val remoteDataSource: IMateriaRemoteDataSource,
) {
    suspend fun fetchMateriasByDocente(email: String): NetworkResult<List<Materia>> = this.remoteDataSource.fetchMateriasByDocente(email)
}
