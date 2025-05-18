package com.example.data

import com.example.data.datastore.ILoginDataStore

class LoginRepository(
    private val store: ILoginDataStore,
) {
    suspend fun doLogin(
        user: String,
        password: String,
    ): Result<Unit> {
        // realiar el login

        //
        store.saveToken("calyr.software@gmail.com")
        return Result.success(Unit)
    }
}
