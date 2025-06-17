package com.medicat.data.registrarAvance

import com.medicat.data.NetworkResult
import com.medicat.domain.Elemento
import com.medicat.domain.Materia
import com.medicat.domain.Recuperatorio

interface IUpdateRemoteDataSource {
    suspend fun updateSaber(
        saberId: Int,
        completado: Boolean,
    ): NetworkResult<Boolean>

    suspend fun updateRecuperatorio(recuperatorio: Recuperatorio): NetworkResult<Boolean>

    suspend fun createRecuperatorio(recuperatorio: Recuperatorio): NetworkResult<Boolean>

    suspend fun deleteRecuperatorio(recuperatorioId: Int): NetworkResult<Boolean>

    suspend fun updateElemento(elemento: Elemento): NetworkResult<Boolean>

    suspend fun updateMateria(
        id: Int,
        recTomados: Int,
        elemCompletados: Int,
        elemEvaluados: Int,
        recTotales: Int,
    ): NetworkResult<Boolean>

    suspend fun getMateria(id: Int): NetworkResult<Materia>
}
