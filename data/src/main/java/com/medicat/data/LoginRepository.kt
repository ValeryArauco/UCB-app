package com.medicat.data

import com.medicat.data.datastore.ILoginDataStore

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
