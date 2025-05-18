package com.example.framework.service

import com.example.framework.dto.MateriaResponseDto
import com.example.framework.dto.UserCheckRequest
import com.example.framework.dto.UserCheckResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IApiService {
    @GET("materia")
    suspend fun getMaterias(): Response<MateriaResponseDto>

    @POST("users/check")
    suspend fun checkUserExists(
        @Body request: UserCheckRequest,
    ): UserCheckResponse
}
