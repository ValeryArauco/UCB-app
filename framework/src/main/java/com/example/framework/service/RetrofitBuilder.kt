package com.example.framework.service

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://firestore.googleapis.com/v1/projects/ucbapp-63925/databases/(default)/documents/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }
    val apiService: IApiService = getRetrofit().create(IApiService::class.java)
}