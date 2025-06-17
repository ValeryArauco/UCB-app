package com.medicat.framework.registrarAvance

import com.medicat.data.NetworkResult
import com.medicat.data.registrarAvance.IUpdateRemoteDataSource
import com.medicat.domain.Elemento
import com.medicat.domain.Materia
import com.medicat.domain.Recuperatorio
import com.medicat.framework.dto.CreateRecuperatorioRequest
import com.medicat.framework.dto.UpdateElementoRequest
import com.medicat.framework.dto.UpdateMateriaRequest
import com.medicat.framework.dto.UpdateRecuperatorioRequest
import com.medicat.framework.mappers.toModel
import com.medicat.framework.service.RetrofitBuilder

class UpdateRemoteDataSource(
    val retrofitService: RetrofitBuilder,
) : IUpdateRemoteDataSource {
    override suspend fun updateSaber(
        saberId: Int,
        completado: Boolean,
    ): NetworkResult<Boolean> {
        val request = mapOf("completado" to completado)

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
                evaluado = elemento.evaluado,
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
        recTotales: Int,
    ): NetworkResult<Boolean> {
        val request =
            UpdateMateriaRequest(
                recTomados = recTomados,
                elemCompletados = elemCompletados,
                elemEvaluados = elemEvaluados,
                recTotales = recTotales,
            )

        val response = retrofitService.apiService.updateMateria(id, request)

        return if (response.isSuccessful && response.body()?.success == true) {
            NetworkResult.Success(true)
        } else {
            val message = response.body()?.message ?: response.message()
            NetworkResult.Error(message)
        }
    }

    override suspend fun getMateria(id: Int): NetworkResult<Materia> {
        val response = retrofitService.apiService.getMateriaById(id)
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()!!.data.map { it.toModel() }[0])
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}
