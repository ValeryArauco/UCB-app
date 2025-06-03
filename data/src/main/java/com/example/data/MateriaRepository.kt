package com.example.data

import com.example.data.materia.IMateriaRemoteDataSource
import com.example.domain.Materia

class MateriaRepository(
    private val remoteDataSource: IMateriaRemoteDataSource,
) {
    suspend fun fetchMateriasByDocente(email: String): NetworkResult<List<Materia>> = this.remoteDataSource.fetchMateriasByDocente(email)
}
