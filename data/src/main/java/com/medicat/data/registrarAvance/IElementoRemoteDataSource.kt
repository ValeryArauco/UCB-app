package com.medicat.data.registrarAvance

import com.medicat.data.NetworkResult
import com.medicat.domain.Elemento

interface IElementoRemoteDataSource {
    suspend fun fetchElementosByMateria(materiaId: String): NetworkResult<List<Elemento>>
}
