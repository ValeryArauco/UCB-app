package com.example.framework.registrarAvance

import com.example.data.NetworkResult
import com.example.data.registrarAvance.IElementoRemoteDataSource
import com.example.domain.Elemento
import com.example.framework.mappers.toModel
import com.example.framework.service.RetrofitBuilder

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
