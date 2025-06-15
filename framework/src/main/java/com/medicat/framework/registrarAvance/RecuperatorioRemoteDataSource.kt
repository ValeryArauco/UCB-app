package com.medicat.framework.registrarAvance

import com.medicat.data.NetworkResult
import com.medicat.data.registrarAvance.IRecuperatorioRemoteDataSource
import com.medicat.domain.Recuperatorio
import com.medicat.framework.mappers.toModel
import com.medicat.framework.service.RetrofitBuilder

class RecuperatorioRemoteDataSource(
    val retrofitService: RetrofitBuilder,
) : IRecuperatorioRemoteDataSource {
    override suspend fun fetchRecuperatorioByElemento(elementoId: String): NetworkResult<List<Recuperatorio>> {
        val response = retrofitService.apiService.fetchRecuperatoriosByElemento(elementoId)
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()!!.data.map { it.toModel() })
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}
