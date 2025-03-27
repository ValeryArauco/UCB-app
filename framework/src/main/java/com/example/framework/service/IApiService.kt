package com.example.framework.service

import com.example.framework.dto.SubjectResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface IApiService {

    @GET("/materia/{subjectId}")
    suspend fun getInfoMateria(@Path("subjectId") subjectId: String): SubjectResponseDto

}