package com.example.data.registrarAvance

import com.example.data.NetworkResult
import com.example.domain.Elemento

interface IElementoRemoteDataSource {
    suspend fun fetchElementosByMateria(materiaId: String): NetworkResult<List<Elemento>>
}
