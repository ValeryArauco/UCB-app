package com.medicat.framework.registrarAvance

import com.medicat.data.NetworkResult
import com.medicat.data.registrarAvance.ISaberRemoteDataSource
import com.medicat.domain.Saber
import com.medicat.framework.mappers.toModel
import com.medicat.framework.service.RetrofitBuilder

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
