package com.example.usecases

class DoLogin {
    suspend fun invoke(
        userName: String,
        password: String,
    ): Boolean = true
}
