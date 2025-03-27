package com.example.ucbapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usecases.DoLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    sealed class LoginState {
        object Init: LoginState()
        object Loading: LoginState()
        class Successful: LoginState()
        class Error(val message: String): LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Init)
    var loginState : StateFlow<LoginState> = _loginState

    val loginUseCase = DoLogin()

    fun doLogin(userName: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            if( loginUseCase.invoke(userName = userName, password = password))
                _loginState.value = LoginState.Successful()
            else
                _loginState.value = LoginState.Error(message = "Invalid credentials")
        }


    }
}
