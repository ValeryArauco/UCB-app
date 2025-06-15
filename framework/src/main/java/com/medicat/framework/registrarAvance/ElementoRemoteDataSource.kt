package com.medicat.framework.registrarAvance

import com.medicat.data.NetworkResult
import com.medicat.data.registrarAvance.IElementoRemoteDataSource
import com.medicat.domain.Elemento
import com.medicat.framework.mappers.toModel
import com.medicat.framework.service.RetrofitBuilder

class ElementoRemoteDataSource(
    val retrofitService: RetrofitBuilder,
) : IElementoRemoteDataSource {
    override suspend fun fetchElementosByMateria(materiaId: String): NetworkResult<List<Elemento>> {
        val response = retrofitService.apiService.fetchElementosByMateria(materiaId)
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()!!.data.map { it.toModel() })
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}
