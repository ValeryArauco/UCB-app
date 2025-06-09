package com.example.data

import com.example.data.login.ILoginRemoteDataSource

class UserRepository(
    private val remoteDataSource: ILoginRemoteDataSource,
) {
    suspend fun isUserAllowed(email: String): NetworkResult<Boolean> = this.remoteDataSource.fetch(email)
}
