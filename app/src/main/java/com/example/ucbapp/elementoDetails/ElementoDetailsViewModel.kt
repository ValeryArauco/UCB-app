package com.example.ucbapp.elementoDetails

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NetworkResult
import com.example.domain.Elemento
import com.example.domain.Recuperatorio
import com.example.domain.Saber
import com.example.usecases.CreateRecuperatorio
import com.example.usecases.DeleteRecuperatorio
import com.example.usecases.GetRecuperatorios
import com.example.usecases.GetSaberes
import com.example.usecases.UpdateElemento
import com.example.usecases.UpdateMateria
import com.example.usecases.UpdateRecuperatorio
import com.example.usecases.UpdateSaber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ElementoDetailsViewModel
    @Inject
    constructor(
        private val getSaberes: GetSaberes,
        private val getRecuperatorios: GetRecuperatorios,
        private val updateSaber: UpdateSaber,
        private val updateRecuperatorio: UpdateRecuperatorio,
        private val createRecuperatorio: CreateRecuperatorio,
        private val deleteRecuperatorio: DeleteRecuperatorio,
        private val updateElemento: UpdateElemento,
        private val updateMateria: UpdateMateria,
    ) : ViewModel() {
        sealed class UIState {
            object Loading : UIState()

            class Loaded(
                val saberes: List<Saber>,
                val recuperatorios: List<Recuperatorio>,
            ) : UIState()

            class Error(
                val message: String,
            ) : UIState()
        }

        private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
        val uiState: StateFlow<UIState> = _uiState

        private val _saberesSeleccionados = MutableStateFlow<Set<Int>>(emptySet())
        val saberesSeleccionados: StateFlow<Set<Int>> = _saberesSeleccionados

        private val _evaluacionFecha = MutableStateFlow(getCurrentDate())
        val evaluacionFecha: StateFlow<String> = _evaluacionFecha

        private val _evaluacionCompletada = MutableStateFlow(false)
        val evaluacionCompletada: StateFlow<Boolean> = _evaluacionCompletada

        private val _comentario = MutableStateFlow("")
        val comentario: StateFlow<String> = _comentario

        private var currentSaberes: List<Saber> = emptyList()
        var currentRecuperatorios: MutableList<Recuperatorio> = mutableListOf()

        private var originalRecuperatorios: List<Recuperatorio> = emptyList()
        private var currentElemento: Elemento? = null

        fun loadData(elemento: Elemento) {
            currentElemento = elemento
            _uiState.value = UIState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val saberesResponse = getSaberes.invoke(elemento.id.toString())
                    val recuperatoriosResponse = getRecuperatorios.invoke(elemento.id.toString())

                    when {
                        saberesResponse is NetworkResult.Error -> {
                            _uiState.value = UIState.Error(saberesResponse.error)
                        }
                        recuperatoriosResponse is NetworkResult.Error -> {
                            _uiState.value = UIState.Error(recuperatoriosResponse.error)
                        }
                        saberesResponse is NetworkResult.Success && recuperatoriosResponse is NetworkResult.Success -> {
                            currentSaberes = saberesResponse.data
                            currentRecuperatorios = recuperatoriosResponse.data.toMutableList()
                            originalRecuperatorios = recuperatoriosResponse.data

                            _evaluacionCompletada.value = elemento.evaluado

                            val saberesCompletados = currentSaberes.filter { it.completado }.map { it.id }.toSet()
                            _saberesSeleccionados.value = saberesCompletados

                            _uiState.value =
                                UIState.Loaded(
                                    saberes = currentSaberes,
                                    recuperatorios = currentRecuperatorios,
                                )
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = UIState.Error(e.message ?: "Error desconocido")
                }
            }
        }

        fun updateRecuperatorioLocal(
            index: Int,
            completado: Boolean? = null,
            fecha: String? = null,
        ) {
            Log.d("ViewModel", "Fecha: $fecha")
            val fechaFormateada = fecha?.let { convertirFecha(it) }
            if (index < currentRecuperatorios.size) {
                val recuperatorio = currentRecuperatorios[index]
                currentRecuperatorios[index] =
                    recuperatorio.copy(
                        completado = completado ?: recuperatorio.completado,
                        fechaEvaluado = fechaFormateada ?: recuperatorio.fechaEvaluado,
                    )

                _uiState.value =
                    UIState.Loaded(
                        saberes = currentSaberes,
                        recuperatorios = currentRecuperatorios.toList(),
                    )
            }
        }

        fun convertirFecha(fechaUI: String): String =
            fechaUI.split("-").let { partes ->
                if (partes.size != 3) fechaUI else "${partes[2]}-${partes[1]}-${partes[0]}"
            }

        fun agregarRecuperatorio(elemento: Elemento) {
            val nuevoRecuperatorio =
                Recuperatorio(
                    id = 0,
                    completado = false,
                    elementoCompetenciaId = elemento.id,
                    fechaEvaluado = null,
                )

            currentRecuperatorios.add(nuevoRecuperatorio)

            _uiState.value =
                UIState.Loaded(
                    saberes = currentSaberes,
                    recuperatorios = currentRecuperatorios.toList(),
                )
        }

        fun eliminarRecuperatorio(index: Int) {
            if (currentRecuperatorios.size > 1 && index < currentRecuperatorios.size) {
                currentRecuperatorios.removeAt(index)

                _uiState.value =
                    UIState.Loaded(
                        saberes = currentSaberes,
                        recuperatorios = currentRecuperatorios.toList(),
                    )
            }
        }

        private fun getRecuperatoriosModificados(): List<Recuperatorio> {
            val modificados = mutableListOf<Recuperatorio>()

            currentRecuperatorios.forEach { current ->
                val original = originalRecuperatorios.find { it.id == current.id }
                if (original != null &&
                    (
                        original.completado != current.completado ||
                            original.fechaEvaluado != current.fechaEvaluado
                    )
                ) {
                    modificados.add(current)
                }
            }

            return modificados
        }

        private fun getRecuperatoriosNuevos(): List<Recuperatorio> = currentRecuperatorios.filter { it.id == 0 }

        private fun getRecuperatoriosTomados(): Int = currentRecuperatorios.filter { it.completado == true }.size

        private fun getSaberesCompletados(): Int = currentSaberes.filter { it.completado == true }.size

        private fun getRecuperatoriosEliminados(): List<Recuperatorio> =
            originalRecuperatorios.filter { original ->
                currentRecuperatorios.none { it.id == original.id }
            }

        @RequiresApi(Build.VERSION_CODES.O)
        fun completarElemento() {
            viewModelScope.launch {
                try {
                    val elemento = currentElemento ?: return@launch

                    val saberIds = _saberesSeleccionados.value
                    for (saberId in saberIds) {
                        val saber = currentSaberes.find { it.id == saberId }
                        if (saber != null && !saber.completado) {
                            when (val result = updateSaber.invoke(saberId)) {
                                is NetworkResult.Success -> {
                                    Log.d("ViewModel", "Saber $saberId actualizado correctamente")
                                }
                                is NetworkResult.Error -> {
                                    Log.e("ViewModel", "Error al actualizar el saber $saberId: ${result.error}")
                                    return@launch
                                }
                            }
                        }
                    }

                    val recuperatoriosNuevos = getRecuperatoriosNuevos()
                    for (recuperatorio in recuperatoriosNuevos) {
                        Log.d("ViewModel", "Recuperatorio $recuperatorio")
                        when (val result = createRecuperatorio.invoke(recuperatorio)) {
                            is NetworkResult.Success -> {
                                Log.d("ViewModel", "Recuperatorio creado correctamente")
                            }
                            is NetworkResult.Error -> {
                                Log.e("ViewModel", "Error al crear recuperatorio: ${result.error}")
                                return@launch
                            }
                        }
                    }

                    val recuperatoriosModificados = getRecuperatoriosModificados()
                    for (recuperatorio in recuperatoriosModificados) {
                        when (val result = updateRecuperatorio.invoke(recuperatorio)) {
                            is NetworkResult.Success -> {
                                Log.d("ViewModel", "Recuperatorio ${recuperatorio.id} actualizado correctamente")
                            }
                            is NetworkResult.Error -> {
                                Log.e("ViewModel", "Error al actualizar recuperatorio ${recuperatorio.id}: ${result.error}")
                                return@launch
                            }
                        }
                    }

                    val recuperatoriosEliminados = getRecuperatoriosEliminados()
                    for (recuperatorio in recuperatoriosEliminados) {
                        when (val result = deleteRecuperatorio.invoke(recuperatorio.id)) {
                            is NetworkResult.Success -> {
                                Log.d("ViewModel", "Recuperatorio ${recuperatorio.id} eliminado")
                            }
                            is NetworkResult.Error -> {
                                Log.e("ViewModel", "Error al eliminar recuperatorio: ${result.error}")
                                return@launch
                            }
                        }
                    }

                    val elementoActualizado =
                        elemento.copy(
                            evaluado = _evaluacionCompletada.value,
                            comentario = _comentario.value,
                            fechaEvaluado = if (_evaluacionCompletada.value) convertirFecha(_evaluacionFecha.value) else null,
                            completado = true,
                            fechaRegistro = LocalDate.now(ZoneId.of("America/La_Paz")).toString(),
                            saberesCompletados = getSaberesCompletados(),
                        )

                    when (val result = updateElemento.invoke(elementoActualizado)) {
                        is NetworkResult.Success -> {
                            Log.d("ViewModel", "Elemento completado exitosamente")
                        }
                        is NetworkResult.Error -> {
                            Log.e("ViewModel", "Error al completar elemento: ${result.error}")
                        }
                    }

                    val recuperatoriosTomados = getRecuperatoriosTomados()
                    val elementosEvaluados = if (_evaluacionCompletada.value) 1 else 0

                    when (val result = updateMateria.invoke(elemento.materiaId.toInt(), recuperatoriosTomados, 1, elementosEvaluados)) {
                        is NetworkResult.Success -> {
                            Log.d("ViewModel", "Materia actualizada exitosamente")
                        }
                        is NetworkResult.Error -> {
                            Log.e("ViewModel", "Error al actualizar Materia: ${result.error}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error general al completar elemento: ${e.message}")
                }
            }
        }

        fun toggleSaber(
            saberId: Int,
            isSelected: Boolean,
        ) {
            val currentSelection = _saberesSeleccionados.value.toMutableSet()
            if (isSelected) {
                currentSelection.add(saberId)
            } else {
                currentSelection.remove(saberId)
            }
            _saberesSeleccionados.value = currentSelection
        }

        fun setEvaluacionFecha(fecha: String) {
            _evaluacionFecha.value = fecha
        }

        fun setEvaluacionCompletada(completada: Boolean) {
            _evaluacionCompletada.value = completada
        }

        fun setComentario(comentario: String) {
            _comentario.value = comentario
        }

        private fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            return dateFormat.format(Date())
        }
    }
