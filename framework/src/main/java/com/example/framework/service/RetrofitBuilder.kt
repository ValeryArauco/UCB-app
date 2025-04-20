package com.example.framework.service

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitBuilder(
    val context: Context,
) {
    private fun getRetrofit(): Retrofit {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(ChuckerInterceptor.Builder(context).build())
                .build()
        return Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
    }

    val apiService: IApiService = getRetrofit().create(IApiService::class.java)

    companion object {
        private const val BASE_URL = "https://firestore.googleapis.com/v1/projects/ucbapp-63925/databases/(default)/documents/"
    }
}
