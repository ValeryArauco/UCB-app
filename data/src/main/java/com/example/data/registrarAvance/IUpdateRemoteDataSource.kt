package com.example.data.registrarAvance

import com.example.data.NetworkResult
import com.example.domain.Elemento
import com.example.domain.Materia
import com.example.domain.Recuperatorio

interface IUpdateRemoteDataSource {
    suspend fun updateSaber(saberId: Int): NetworkResult<Boolean>

    suspend fun updateRecuperatorio(recuperatorio: Recuperatorio): NetworkResult<Boolean>

    suspend fun createRecuperatorio(recuperatorio: Recuperatorio): NetworkResult<Boolean>

    suspend fun deleteRecuperatorio(recuperatorioId: Int): NetworkResult<Boolean>

    suspend fun updateElemento(elemento: Elemento): NetworkResult<Boolean>

    suspend fun updateMateria(
        id: Int,
        recTomados: Int,
        elemCompletados: Int,
        elemEvaluados: Int,
    ): NetworkResult<Boolean>

    suspend fun getMateria(id: Int): NetworkResult<Materia>
}
