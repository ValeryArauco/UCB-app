package com.example.framework.registrarAvance

import com.example.data.NetworkResult
import com.example.data.registrarAvance.IMateriaRemoteDataSource
import com.example.domain.Materia
import com.example.framework.mappers.toModel
import com.example.framework.service.RetrofitBuilder

class MateriaRemoteDataSource(
    val retrofitService: RetrofitBuilder,
) : IMateriaRemoteDataSource {
    override suspend fun fetchMateriasByDocente(email: String): NetworkResult<List<Materia>> {
        val response = retrofitService.apiService.fetchMateriasByDocente(email)
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()!!.data.map { it.toModel() })
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}
