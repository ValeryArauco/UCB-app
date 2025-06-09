package com.example.framework.registrarAvance

import com.example.data.NetworkResult
import com.example.data.registrarAvance.IUpdateRemoteDataSource
import com.example.domain.Elemento
import com.example.domain.Recuperatorio
import com.example.framework.dto.CreateRecuperatorioRequest
import com.example.framework.dto.UpdateElementoRequest
import com.example.framework.dto.UpdateMateriaRequest
import com.example.framework.dto.UpdateRecuperatorioRequest
import com.example.framework.service.RetrofitBuilder

class UpdateRemoteDataSource(
    val retrofitService: RetrofitBuilder,
) : IUpdateRemoteDataSource {
    override suspend fun updateSaber(saberId: Int): NetworkResult<Boolean> {
        val request = mapOf("completado" to true)

        val response = retrofitService.apiService.updateSaber(saberId, request)

        return if (response.isSuccessful && response.body()?.success == true) {
            NetworkResult.Success(true)
        } else {
            val message = response.body()?.message ?: response.message()
            NetworkResult.Error(message)
        }
    }

    override suspend fun updateRecuperatorio(recuperatorio: Recuperatorio): NetworkResult<Boolean> {
        val request =
            UpdateRecuperatorioRequest(
                completado = recuperatorio.completado,
                fechaEvaluado = recuperatorio.fechaEvaluado,
            )

        val response = retrofitService.apiService.updateRecuperatorio(recuperatorio.id, request)

        return if (response.isSuccessful && response.body()?.success == true) {
            NetworkResult.Success(true)
        } else {
            val message = response.body()?.message ?: response.message()
            NetworkResult.Error(message)
        }
    }

    override suspend fun createRecuperatorio(recuperatorio: Recuperatorio): NetworkResult<Boolean> {
        val request =
            CreateRecuperatorioRequest(
                completado = recuperatorio.completado,
                elementoCompetenciaId = recuperatorio.elementoCompetenciaId,
                fechaEvaluado = recuperatorio.fechaEvaluado,
            )

        val response = retrofitService.apiService.createRecuperatorio(request)

        return if (response.isSuccessful && response.body()?.success == true) {
            NetworkResult.Success(true)
        } else {
            val message = response.body()?.message ?: response.message()
            NetworkResult.Error(message)
        }
    }

    override suspend fun deleteRecuperatorio(recuperatorioId: Int): NetworkResult<Boolean> {
        val response = retrofitService.apiService.deleteRecuperatorio(recuperatorioId)

        return if (response.isSuccessful && response.body()?.success == true) {
            NetworkResult.Success(true)
        } else {
            val message = response.body()?.message ?: response.message()
            NetworkResult.Error(message)
        }
    }

    override suspend fun updateElemento(elemento: Elemento): NetworkResult<Boolean> {
        val request =
            UpdateElementoRequest(
                fechaEvaluado = elemento.fechaEvaluado,
                fechaRegistro = elemento.fechaRegistro,
                saberesCompletados = elemento.saberesCompletados,
                completado = elemento.completado,
                evaluado = elemento.completado,
                comentario = elemento.comentario,
            )

        val response = retrofitService.apiService.updateElemento(elemento.id, request)

        return if (response.isSuccessful && response.body()?.success == true) {
            NetworkResult.Success(true)
        } else {
            val message = response.body()?.message ?: response.message()
            NetworkResult.Error(message)
        }
    }

    override suspend fun updateMateria(
        id: Int,
        recTomados: Int,
        elemCompletados: Int,
        elemEvaluados: Int,
    ): NetworkResult<Boolean> {
        val request =
            UpdateMateriaRequest(
                recTomados = recTomados,
                elemCompletados = elemCompletados,
                elemEvaluados = elemEvaluados,
            )

        val response = retrofitService.apiService.updateMateria(id, request)

        return if (response.isSuccessful && response.body()?.success == true) {
            NetworkResult.Success(true)
        } else {
            val message = response.body()?.message ?: response.message()
            NetworkResult.Error(message)
        }
    }
}
