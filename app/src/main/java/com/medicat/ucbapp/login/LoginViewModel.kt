package com.medicat.ucbapp.login

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.medicat.data.NetworkResult
import com.medicat.ucbapp.service.GoogleSignInUtils
import com.medicat.ucbapp.service.InternetConnection.Companion.isConnected
import com.medicat.usecases.IsUserAllowed
import com.medicat.usecases.ObtainToken
import com.medicat.usecases.UpdateToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val isUserAllowed: IsUserAllowed,
        private val obtainToken: ObtainToken,
        private val updateToken: UpdateToken,
    ) : ViewModel() {
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
                return
            }

            GoogleSignInUtils.doGoogleSignIn(
                context = context,
                scope = viewModelScope,
                launcher = launcher,
                login = {
                    viewModelScope.launch {
                        val email = Firebase.auth.currentUser?.email ?: ""
                        Log.d("LoginViewModel", "Verificando email: $email")
                        val response = isUserAllowed.invoke(email)
                        Log.d("LoginViewModel", "Verificando response: $response")
                        val allowed = (response as? NetworkResult.Success<Boolean>)?.data == true
                        Log.d("LoginViewModel", "Verificando allowed: $allowed")
                        if (allowed) {
                            val token = obtainToken.getToken()
                            updateToken.invoke(email, token)
                            _loginState.value = LoginState.Successful()
                        } else {
                            Firebase.auth.signOut()
                            _loginState.value = LoginState.Error("Usuario no autorizado")
                        }
                    }
                },
            )
        }

        fun signInWithEmailPassword(
            email: String,
            password: String,
        ) {
            _loginState.value = LoginState.Loading

            viewModelScope.launch {
                try {
                    val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
                    val user = authResult.user

                    if (user != null) {
                        val response = isUserAllowed.invoke(email)
                        val allowed = (response as? NetworkResult.Success<Boolean>)?.data == true

                        if (allowed) {
                            val token = obtainToken.getToken()
                            updateToken.invoke(email, token)
                            _loginState.value = LoginState.Successful()
                        } else {
                            Firebase.auth.signOut()
                            _loginState.value = LoginState.Error("Usuario no autorizado")
                        }
                    } else {
                        _loginState.value = LoginState.Error("Error en la autenticación")
                    }
                } catch (e: Exception) {
                    Log.e("LoginViewModel", "Error en login con email/password", e)

                    val errorMessage =
                        when (e) {
                            is com.google.firebase.auth.FirebaseAuthInvalidUserException ->
                                "Usuario no encontrado. Contacte al administrador para crear su cuenta."
                            is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                                "Credenciales inválidas. Verifique su email y contraseña."
                            else -> "Error de conexión. Verifique su internet e intente nuevamente."
                        }

                    _loginState.value = LoginState.Error(errorMessage)
                }
            }
        }
    }
