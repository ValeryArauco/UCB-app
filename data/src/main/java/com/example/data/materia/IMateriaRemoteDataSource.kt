package com.example.data.materia
import com.example.data.NetworkResult
import com.example.domain.Materia

interface IMateriaRemoteDataSource {
    suspend fun fetchMateriasByDocente(email: String): NetworkResult<List<Materia>>
}
