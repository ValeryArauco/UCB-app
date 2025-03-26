package com.example.ucbapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {
    private val _isLightMode = MutableStateFlow(true)
    val isLightMode: StateFlow<Boolean> = _isLightMode

    fun toggleTheme(isLightMode: Boolean) {
        viewModelScope.launch {
            _isLightMode.value = isLightMode
        }
    }
}