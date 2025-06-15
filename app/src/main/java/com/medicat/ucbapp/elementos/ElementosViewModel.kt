package com.medicat.ucbapp.elementos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicat.data.NetworkResult
import com.medicat.domain.Elemento
import com.medicat.domain.Materia
import com.medicat.usecases.GetElementos
import com.medicat.usecases.GetMateria
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElementosViewModel
    @Inject
    constructor(
        private val getElementos: GetElementos,
        private val getMateria: GetMateria,
    ) : ViewModel() {
        sealed class ElementosUIState {
            object Loading : ElementosUIState()

            class Loaded(
                val elementos: List<Elemento>,
                val materia: Materia? = null,
            ) : ElementosUIState()

            class Error(
                val message: String,
            ) : ElementosUIState()
        }

        private val _uiState = MutableStateFlow<ElementosUIState>(ElementosUIState.Loading)
        val uiState: StateFlow<ElementosUIState> = _uiState

        fun loadElementos(materia: Materia) {
            _uiState.value = ElementosUIState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                val response = getElementos.invoke(materia.id)

                when (val result = response) {
                    is NetworkResult.Error -> {
                        _uiState.value = ElementosUIState.Error(result.error)
                    }
                    is NetworkResult.Success -> {
                        _uiState.value = ElementosUIState.Loaded(result.data)
                    }
                }
            }
        }

        fun refreshMateriaData(materia: Materia) {
            viewModelScope.launch {
                try {
                    val result = getMateria.invoke(materia.id.toInt())

                    when (result) {
                        is NetworkResult.Success -> {
                            val updatedMateria = result.data

                            val currentState = _uiState.value
                            if (currentState is ElementosUIState.Loaded) {
                                _uiState.value =
                                    ElementosUIState.Loaded(
                                        elementos = currentState.elementos,
                                        materia = updatedMateria,
                                    )
                            }
                        }
                        is NetworkResult.Error -> {
                        }
                    }
                } catch (e: Exception) {
                }
            }
        }
    }
