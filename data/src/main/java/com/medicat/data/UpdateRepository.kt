package com.medicat.data

import com.medicat.data.registrarAvance.IUpdateRemoteDataSource
import com.medicat.domain.Elemento
import com.medicat.domain.Materia
import com.medicat.domain.Recuperatorio

class UpdateRepository(
    private val remoteDataSource: IUpdateRemoteDataSource,
) {
    suspend fun updateSaber(
        saberId: Int,
        completado: Boolean,
    ): NetworkResult<Boolean> = this.remoteDataSource.updateSaber(saberId, completado)

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
        recTotales: Int,
    ): NetworkResult<Boolean> =
        this.remoteDataSource.updateMateria(
            id,
            recTomados,
            elemCompletados,
            elemEvaluados,
            recTotales,
        )

    suspend fun getMateria(id: Int): NetworkResult<Materia> = this.remoteDataSource.getMateria(id)
}
