package com.example.framework.service

import com.example.framework.dto.MateriaResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiService {

    @GET("/materia")
    suspend fun getMaterias(): Response<MateriaResponseDto>

}