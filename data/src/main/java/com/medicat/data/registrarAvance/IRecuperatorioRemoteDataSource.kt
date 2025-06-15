package com.medicat.data.registrarAvance

import com.medicat.data.NetworkResult
import com.medicat.domain.Recuperatorio

interface IRecuperatorioRemoteDataSource {
    suspend fun fetchRecuperatorioByElemento(elementoId: String): NetworkResult<List<Recuperatorio>>
}
