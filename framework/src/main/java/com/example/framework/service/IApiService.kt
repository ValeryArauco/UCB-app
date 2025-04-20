package com.example.framework.service

import com.example.framework.dto.MateriaResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface IApiService {
    @GET("materia")
    suspend fun getMaterias(): Response<MateriaResponseDto>
}
