package com.uob.framework.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://api.example.com/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}