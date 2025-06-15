package com.medicat.data

import com.medicat.data.registrarAvance.IElementoRemoteDataSource
import com.medicat.domain.Elemento

class ElementoRepository(
    private val remoteDataSource: IElementoRemoteDataSource,
) {
    suspend fun fetchElementosByMateria(materiaId: String): NetworkResult<List<Elemento>> =
        this.remoteDataSource.fetchElementosByMateria(
            materiaId,
        )
}
