package com.example.data

import com.example.data.registrarAvance.IUpdateRemoteDataSource
import com.example.domain.Elemento
import com.example.domain.Materia
import com.example.domain.Recuperatorio

class UpdateRepository(
    private val remoteDataSource: IUpdateRemoteDataSource,
) {
    suspend fun updateSaber(saberId: Int): NetworkResult<Boolean> = this.remoteDataSource.updateSaber(saberId)

    suspend fun updateRecuperatorio(recuperatorio: Recuperatorio): NetworkResult<Boolean> =
        this.remoteDataSource.updateRecuperatorio(
            recuperatorio,
        )

    suspend fun createRecuperatorio(recuperatorio: Recuperatorio): NetworkResult<Boolean> =
        this.remoteDataSource.createRecuperatorio(
            recuperatorio,
        )

    suspend fun deleteRecuperatorio(recuperatorioId: Int): NetworkResult<Boolean> =
        this.remoteDataSource.deleteRecuperatorio(
            recuperatorioId,
        )

    suspend fun updateElemento(elemento: Elemento): NetworkResult<Boolean> =
        this.remoteDataSource.updateElemento(
            elemento,
        )

    suspend fun updateMateria(
        id: Int,
        recTomados: Int,
        elemCompletados: Int,
        elemEvaluados: Int,
    ): NetworkResult<Boolean> =
        this.remoteDataSource.updateMateria(
            id,
            recTomados,
            elemCompletados,
            elemEvaluados,
        )

    suspend fun getMateria(id: Int): NetworkResult<Materia> = this.remoteDataSource.getMateria(id)
}
