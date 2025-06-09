package com.example.data.registrarAvance

import com.example.data.NetworkResult
import com.example.domain.Saber

interface ISaberRemoteDataSource {
    suspend fun fetchSaberesByElemento(elementoId: String): NetworkResult<List<Saber>>
}
