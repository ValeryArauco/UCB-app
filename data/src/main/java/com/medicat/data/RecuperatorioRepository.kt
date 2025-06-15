package com.medicat.data

import com.medicat.data.registrarAvance.IRecuperatorioRemoteDataSource
import com.medicat.domain.Recuperatorio

class RecuperatorioRepository(
    private val remoteDataSource: IRecuperatorioRemoteDataSource,
) {
    suspend fun fetchRecuperatoriosByElemento(elementoId: String): NetworkResult<List<Recuperatorio>> =
        this.remoteDataSource.fetchRecuperatorioByElemento(
            elementoId,
        )
} 
