package com.example.framework.service

import com.example.framework.dto.MateriaResponseDto
import com.example.framework.dto.UserCheckResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiService {
    @GET("materias")
    suspend fun fetchMateriasByDocente(
        @Query("email") email: String,
    ): Response<MateriaResponseDto>

    @GET("validate-email")
    suspend fun isUserAllowed(
        @Query("email") email: String,
    ): Response<UserCheckResponse>
}
