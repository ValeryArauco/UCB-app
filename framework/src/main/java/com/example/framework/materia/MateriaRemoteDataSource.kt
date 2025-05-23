package com.example.framework.materia

import com.example.data.NetworkResult
import com.example.data.materia.IMateriaRemoteDataSource
import com.example.domain.Materia
import com.example.framework.mappers.toModel
import com.example.framework.service.RetrofitBuilder

class MateriaRemoteDataSource(val retrofitService: RetrofitBuilder): IMateriaRemoteDataSource{
    override suspend fun getMaterias(): NetworkResult<List<Materia>>{
        val response = retrofitService.apiService.getMaterias()
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()!!.documents.map { it.toModel() })
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}

