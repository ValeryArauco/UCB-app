package com.example.ucbapp.materias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NetworkResult
import com.example.domain.Materia
import com.example.usecases.GetMaterias
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MateriasViewModel
    @Inject
    constructor(
        private val getMaterias: GetMaterias,
    ) : ViewModel() {
        sealed class MateriasUIState {
            object Loading : MateriasUIState()

            class Loaded(
                val materias: List<Materia>,
            ) : MateriasUIState()

            class Error(
                val message: String,
            ) : MateriasUIState()
        }

        private val _uiState = MutableStateFlow<MateriasUIState>(MateriasUIState.Loading)
        val uiState: StateFlow<MateriasUIState> = _uiState

        fun loadMaterias() {
            _uiState.value = MateriasUIState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                val response = getMaterias.invoke()

                when (val result = response) {
                    is NetworkResult.Error -> {
                        _uiState.value = MateriasUIState.Error(result.error)
                    }
                    is NetworkResult.Success -> {
                        _uiState.value = MateriasUIState.Loaded(result.data)
                    }
                }
            }
        }
    }
