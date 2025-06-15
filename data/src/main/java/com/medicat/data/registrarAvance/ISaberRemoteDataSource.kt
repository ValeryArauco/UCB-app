package com.medicat.data.registrarAvance

import com.medicat.data.NetworkResult
import com.medicat.domain.Saber

interface ISaberRemoteDataSource {
    suspend fun fetchSaberesByElemento(elementoId: String): NetworkResult<List<Saber>>
}
