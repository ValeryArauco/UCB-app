package com.example.data.login

import com.example.data.NetworkResult

interface ILoginRemoteDataSource {
    suspend fun fetch(email: String): NetworkResult<Boolean>
}
