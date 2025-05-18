package com.example.framework.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.datastore.ILoginDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(
    name = "user_preferences",
)

class LoginDataSource(
    private val context: Context,
) : ILoginDataStore {
    companion object {
        private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore("auth_prefs")
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    override suspend fun saveToken(token: String) {
        context.authDataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    val tokenFlow: Flow<String?> =
        context.authDataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }

    override suspend fun clearToken() {
        context.authDataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}
