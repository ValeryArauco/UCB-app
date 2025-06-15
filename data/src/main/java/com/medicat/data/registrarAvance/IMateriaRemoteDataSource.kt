package com.medicat.data.registrarAvance
import com.medicat.data.NetworkResult
import com.medicat.domain.Materia

interface IMateriaRemoteDataSource {
    suspend fun fetchMateriasByDocente(email: String): NetworkResult<List<Materia>>
}
