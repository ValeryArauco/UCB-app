package com.example.framework.registrarAvance

import com.example.data.NetworkResult
import com.example.data.registrarAvance.IRecuperatorioRemoteDataSource
import com.example.domain.Recuperatorio
import com.example.framework.mappers.toModel
import com.example.framework.service.RetrofitBuilder

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
