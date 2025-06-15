package com.medicat.data.login

import com.medicat.data.NetworkResult

interface ILoginRemoteDataSource {
    suspend fun fetch(email: String): NetworkResult<Boolean>
}
