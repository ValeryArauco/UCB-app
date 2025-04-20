package com.example.ucbapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usecases.DoLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        val loginUseCase: DoLogin,
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
        var loginState: StateFlow<LoginState> = _loginState

        fun doLogin(
            userName: String,
            password: String,
        ) {
            _loginState.value = LoginState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                val result: Result<Unit> = loginUseCase.invoke(userName = userName, password = password)

                when {
                    result.isSuccess -> {
                        _loginState.value = LoginState.Successful()
                    }
                    result.isFailure -> {
                        _loginState.value = LoginState.Error(message = "Invalid credentials")
                    }
                }
            }
        }
    }
