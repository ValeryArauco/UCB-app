package com.example.data.registrarAvance

import com.example.data.NetworkResult
import com.example.domain.Recuperatorio

interface IRecuperatorioRemoteDataSource {
    suspend fun fetchRecuperatorioByElemento(elementoId: String): NetworkResult<List<Recuperatorio>>
}
