package com.example.framework.registrarAvance

import com.example.data.NetworkResult
import com.example.data.registrarAvance.ISaberRemoteDataSource
import com.example.domain.Saber
import com.example.framework.mappers.toModel
import com.example.framework.service.RetrofitBuilder

class SaberRemoteDataSource(
    val retrofitService: RetrofitBuilder,
) : ISaberRemoteDataSource {
    override suspend fun fetchSaberesByElemento(elementoId: String): NetworkResult<List<Saber>> {
        val response = retrofitService.apiService.fetchSaberesByElemento(elementoId)
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()!!.data.map { it.toModel() })
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}
