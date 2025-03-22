package com.uob.framework.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AuthService {
    suspend fun logout()
}

class AuthServiceImpl : AuthService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://yourapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(AuthApi::class.java)

    override suspend fun logout() {
        api.logout()
    }
}