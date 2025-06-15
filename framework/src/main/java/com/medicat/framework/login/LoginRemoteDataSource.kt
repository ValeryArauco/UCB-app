package com.medicat.framework.login

import android.util.Log
import com.medicat.data.NetworkResult
import com.medicat.data.login.ILoginRemoteDataSource
import com.medicat.framework.service.RetrofitBuilder

class LoginRemoteDataSource(
    val retrofitService: RetrofitBuilder,
) : ILoginRemoteDataSource {
    override suspend fun fetch(email: String): NetworkResult<Boolean> {
        Log.d("LoginViewModel", "Response entra: $email")
        val response = retrofitService.apiService.isUserAllowed(email)
        Log.d("LoginViewModel", "Response impl: $response")
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()?.exists ?: return NetworkResult.Error("Respuesta vacía del servidor"))
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}
