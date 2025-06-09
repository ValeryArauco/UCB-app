package com.example.data

import com.example.data.registrarAvance.IRecuperatorioRemoteDataSource
import com.example.domain.Recuperatorio

class RecuperatorioRepository(
    private val remoteDataSource: IRecuperatorioRemoteDataSource,
) {
    suspend fun fetchRecuperatoriosByElemento(elementoId: String): NetworkResult<List<Recuperatorio>> =
        this.remoteDataSource.fetchRecuperatorioByElemento(
            elementoId,
        )
} 
