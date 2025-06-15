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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val isUserAllowed: IsUserAllowed,
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
                            _loginState.value = LoginState.Successful()
                        } else {
                            Firebase.auth.signOut()
                            _loginState.value = LoginState.Error("Usuario no autorizado")
                        }
                    }
                },
            )
        }
    }
