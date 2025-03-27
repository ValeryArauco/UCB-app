package com.example.usecases


class DoLogin {
    suspend fun invoke(userName: String, password: String) : Boolean {
        return (userName.equals("calyr") && password.equals("123456"))
    }
}