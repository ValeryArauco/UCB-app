package com.medicat.data

import com.medicat.data.registrarAvance.ISaberRemoteDataSource
import com.medicat.domain.Saber

class SaberRepository(
    private val remoteDataSource: ISaberRemoteDataSource,
) {
    suspend fun fetchSaberByElemento(elementoId: String): NetworkResult<List<Saber>> =
        this.remoteDataSource.fetchSaberesByElemento(
            elementoId,
        )
}
