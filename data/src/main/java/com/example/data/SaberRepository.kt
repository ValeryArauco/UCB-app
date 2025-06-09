package com.example.data

import com.example.data.registrarAvance.ISaberRemoteDataSource
import com.example.domain.Saber

class SaberRepository(
    private val remoteDataSource: ISaberRemoteDataSource,
) {
    suspend fun fetchSaberByElemento(elementoId: String): NetworkResult<List<Saber>> =
        this.remoteDataSource.fetchSaberesByElemento(
            elementoId,
        )
}
