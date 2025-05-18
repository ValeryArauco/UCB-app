package com.example.ucbapp.login

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.framework.dto.UserCheckRequest
import com.example.framework.service.IApiService
import com.example.ucbapp.service.GoogleSignInUtils
import com.example.ucbapp.service.InternetConnection.Companion.isConnected
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor() : ViewModel() {
        sealed class LoginState {
            object Init : LoginState()

            object Loading : LoginState()

            class Successful : LoginState()

            class Error(
                val message: String,
            ) : LoginState()
        }

        private val _loginState = MutableStateFlow<LoginState>(LoginState.Init)
        val loginState: StateFlow<LoginState> = _loginState

        fun signInWithGoogle(
            context: Context,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        ) {
            _loginState.value = LoginState.Loading

            if (!isConnected(context)) {
                _loginState.value = LoginState.Error("No tiene acceso a internet")
            } else {
                GoogleSignInUtils.doGoogleSignIn(
                    context = context,
                    scope = viewModelScope,
                    launcher = launcher,
                    login = {
                        _loginState.value = LoginState.Successful()
                    },
                )
            }
        }

        fun handleSignInResult(result: ActivityResult) {
            try {
                val credential = GoogleAuthProvider.getCredential(result.data?.getStringExtra("token"), null)
                Firebase.auth
                    .signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Verificar dominio del correo
                            if (task.result
                                    ?.user
                                    ?.email
                                    ?.endsWith("@ucb.edu.bo") == true
                            ) {
                                _loginState.value = LoginState.Successful()
                            } else {
                                _loginState.value = LoginState.Error("Solo correos institucionales")
                            }
                        }
                    }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Error desconocido")
            }
        }

        fun resetState() {
        }
    }

class UserRepositoryImpl
    @Inject
    constructor(
        private val apiService: IApiService,
    ) : UserRepository {
        override suspend fun checkUserExists(email: String): Boolean =
            try {
                val response = apiService.checkUserExists(UserCheckRequest(email))
                response.exists
            } catch (e: Exception) {
                Log.e("UserRepository", "Error verificando usuario: ${e.message}")
                false // En caso de error de red, asumimos que el usuario no existe
            }
    }

interface UserRepository {
    suspend fun checkUserExists(email: String): Boolean
}
