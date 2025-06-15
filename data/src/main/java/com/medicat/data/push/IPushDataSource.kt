package com.medicat.data.push

interface IPushDataSource {
    suspend fun getToken(): String
}
