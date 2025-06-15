package com.medicat.framework.registrarAvance

import com.medicat.data.NetworkResult
import com.medicat.data.registrarAvance.IMateriaRemoteDataSource
import com.medicat.domain.Materia
import com.medicat.framework.mappers.toModel
import com.medicat.framework.service.RetrofitBuilder

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
