package com.medicat.framework.service

import com.medicat.framework.dto.ApiResponseDto
import com.medicat.framework.dto.CreateRecuperatorioRequest
import com.medicat.framework.dto.ElementoDto
import com.medicat.framework.dto.MateriaDto
import com.medicat.framework.dto.RecuperatorioDto
import com.medicat.framework.dto.ResponseDto
import com.medicat.framework.dto.SaberDto
import com.medicat.framework.dto.UpdateElementoRequest
import com.medicat.framework.dto.UpdateMateriaRequest
import com.medicat.framework.dto.UpdateRecuperatorioRequest
import com.medicat.framework.dto.UserCheckResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {
    @GET("materia/{id}")
    suspend fun getMateriaById(
        @Path("id") id: Int,
    ): Response<ResponseDto<MateriaDto>>

    @PATCH("materia/{id}/increment")
    suspend fun updateMateria(
        @Path("id") id: Int,
        @Body body: UpdateMateriaRequest,
    ): Response<ApiResponseDto>

    @PATCH("elemento/{id}")
    suspend fun updateElemento(
        @Path("id") id: Int,
        @Body body: UpdateElementoRequest,
    ): Response<ApiResponseDto>

    @DELETE("recuperatorio/{id}")
    suspend fun deleteRecuperatorio(
        @Path("id") id: Int,
    ): Response<ApiResponseDto>

    @POST("recuperatorio")
    suspend fun createRecuperatorio(
        @Body body: CreateRecuperatorioRequest,
    ): Response<ApiResponseDto>

    @PATCH("recuperatorio/{id}")
    suspend fun updateRecuperatorio(
        @Path("id") id: Int,
        @Body body: UpdateRecuperatorioRequest,
    ): Response<ApiResponseDto>

    @PATCH("saber/{id}/completado")
    suspend fun updateSaber(
        @Path("id") id: Int,
        @Body body: Map<String, Boolean>,
    ): Response<ApiResponseDto>

    @GET("recuperatorios")
    suspend fun fetchRecuperatoriosByElemento(
        @Query("elemento") elementoId: String,
    ): Response<ResponseDto<RecuperatorioDto>>

    @GET("saberes")
    suspend fun fetchSaberesByElemento(
        @Query("elemento") elementoId: String,
    ): Response<ResponseDto<SaberDto>>

    @GET("elementos")
    suspend fun fetchElementosByMateria(
        @Query("materia") materiaId: String,
    ): Response<ResponseDto<ElementoDto>>

    @GET("materias")
    suspend fun fetchMateriasByDocente(
        @Query("email") email: String,
    ): Response<ResponseDto<MateriaDto>>

    @GET("validate-email")
    suspend fun isUserAllowed(
        @Query("email") email: String,
    ): Response<UserCheckResponse>
}
