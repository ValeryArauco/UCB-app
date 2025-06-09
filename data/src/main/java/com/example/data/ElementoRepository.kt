package com.example.data

import com.example.data.registrarAvance.IElementoRemoteDataSource
import com.example.domain.Elemento

class ElementoRepository(
    private val remoteDataSource: IElementoRemoteDataSource,
) {
    suspend fun fetchElementosByMateria(materiaId: String): NetworkResult<List<Elemento>> =
        this.remoteDataSource.fetchElementosByMateria(
            materiaId,
        )
}
