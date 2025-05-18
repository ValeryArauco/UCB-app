package com.example.data.push

interface IPushDataSource {
    suspend fun getToken(): String
}
