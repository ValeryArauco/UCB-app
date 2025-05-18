package com.example.data.datastore

interface ILoginDataStore {
    suspend fun saveToken(token: String)

    suspend fun clearToken()
}
