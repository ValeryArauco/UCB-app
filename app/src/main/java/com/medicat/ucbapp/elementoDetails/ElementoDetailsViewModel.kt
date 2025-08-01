package com.medicat.ucbapp.elementoDetails

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicat.data.NetworkResult
import com.medicat.domain.Elemento
import com.medicat.domain.Recuperatorio
import com.medicat.domain.Saber
import com.medicat.usecases.CreateRecuperatorio
import com.medicat.usecases.DeleteRecuperatorio
import com.medicat.usecases.GetRecuperatorios
import com.medicat.usecases.GetSaberes
import com.medicat.usecases.UpdateElemento
import com.medicat.usecases.UpdateMateria
import com.medicat.usecases.UpdateRecuperatorio
import com.medicat.usecases.UpdateSaber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
                val progreso: ProgresoElemento,
            ) : UIState()

            class Error(
                val message: String,
            ) : UIState()
        }

        data class ProgresoElemento(
            val tieneSaberes: Boolean,
            val estaEvaluado: Boolean,
            val tieneRecuperatorios: Boolean,
            val puedeCompletarse: Boolean,
            val hayPendientes: Boolean,
        )

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

        private val _isGuardando = MutableStateFlow(false)
        val isGuardando: StateFlow<Boolean> = _isGuardando

        private var currentSaberes: List<Saber> = emptyList()
        var currentRecuperatorios: MutableList<Recuperatorio> = mutableListOf()

        private var originalSaberes: List<Saber> = emptyList()
        private var originalRecuperatorios: List<Recuperatorio> = emptyList()
        private var originalSaberesSeleccionados: Set<Int> = emptySet()
        private var originalEvaluacionCompletada: Boolean = false
        private var originalComentario: String = ""
        private var originalElementoCompletado: Boolean = false

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

                            originalSaberes = saberesResponse.data
                            originalRecuperatorios = recuperatoriosResponse.data
                            originalEvaluacionCompletada = elemento.evaluado
                            originalComentario = elemento.comentario ?: ""
                            originalElementoCompletado = elemento.completado

                            _evaluacionCompletada.value = elemento.evaluado
                            _evaluacionFecha.value =
                                elemento.fechaEvaluado?.let { convertirFecha(it) }.toString()

                            Log.d("date", _evaluacionFecha.value)
                            _comentario.value = elemento.comentario ?: ""

                            val saberesCompletados = currentSaberes.filter { it.completado }.map { it.id }.toSet()
                            _saberesSeleccionados.value = saberesCompletados
                            originalSaberesSeleccionados = saberesCompletados

                            updateUIState()
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = UIState.Error(e.message ?: "Error desconocido")
                }
            }
        }

        private fun updateUIState() {
            val progreso = calcularProgreso()
            _uiState.value =
                UIState.Loaded(
                    saberes = currentSaberes,
                    recuperatorios = currentRecuperatorios.toList(),
                    progreso = progreso,
                )
        }

        private fun calcularProgreso(): ProgresoElemento {
            val tieneSaberes = _saberesSeleccionados.value.isNotEmpty()
            val estaEvaluado = _evaluacionCompletada.value
            val tieneRecuperatorios = currentRecuperatorios.any { it.completado }
            val puedeCompletarse = tieneSaberes && estaEvaluado && tieneRecuperatorios

            val cambiosEnSaberes = _saberesSeleccionados.value != originalSaberesSeleccionados
            val cambiosEnEvaluacion =
                _evaluacionCompletada.value != originalEvaluacionCompletada ||
                    _comentario.value != originalComentario
            val cambiosEnRecuperatorios = tieneRecuperatoriosPendientes()

            val hayPendientes = cambiosEnSaberes || cambiosEnEvaluacion || cambiosEnRecuperatorios

            return ProgresoElemento(
                tieneSaberes = tieneSaberes,
                estaEvaluado = estaEvaluado,
                tieneRecuperatorios = tieneRecuperatorios,
                puedeCompletarse = puedeCompletarse,
                hayPendientes = hayPendientes,
            )
        }

        private fun tieneRecuperatoriosPendientes(): Boolean {
            if (currentRecuperatorios.any { it.id == 0 }) return true

            if (originalRecuperatorios.any { original ->
                    currentRecuperatorios.none { it.id == original.id }
                }
            ) {
                return true
            }

            return currentRecuperatorios.any { current ->
                current.id != 0 &&
                    originalRecuperatorios.find { it.id == current.id }?.let { original ->
                        original.completado != current.completado ||
                            original.fechaEvaluado != current.fechaEvaluado
                    } ?: false
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun guardarCambios(): Job {
            return viewModelScope.launch(Dispatchers.IO) {
                _isGuardando.value = true
                try {
                    val elemento =
                        currentElemento ?: run {
                            _uiState.value = UIState.Error("No hay elemento cargado")
                            return@launch
                        }

                    if (_saberesSeleccionados.value != originalSaberesSeleccionados) {
                        Log.d("ViewModel", "Guardando cambios en saberes...")
                        guardarSaberesInterno()
                    }

                    Log.d("ViewModel", "Guardando cambios en evaluación...")
                    guardarEvaluacionInterno(elemento)

                    if (tieneRecuperatoriosPendientes()) {
                        Log.d("ViewModel", "Guardando cambios en recuperatorios...")
                        guardarRecuperatoriosInterno(elemento)
                    }

                    actualizarEstadoCompletado(elemento)

                    actualizarEstadosOriginales()

                    updateUIState()
                    Log.d("ViewModel", "Todos los cambios guardados exitosamente")
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error al guardar cambios: ${e.message}")
                    _uiState.value = UIState.Error("Error al guardar: ${e.message}")
                } finally {
                    _isGuardando.value = false
                }
            }
        }

        private suspend fun guardarSaberesInterno() {
            val elemento = currentElemento ?: throw Exception("No hay elemento cargado")
            val saberesAActualizar = getSaberesModificados()

            for ((saberId, completado) in saberesAActualizar) {
                when (val result = updateSaber.invoke(saberId, completado)) {
                    is NetworkResult.Success -> {
                        Log.d("ViewModel", "Saber $saberId actualizado: completado=$completado")
                    }
                    is NetworkResult.Error -> {
                        throw Exception("Error al actualizar saber $saberId: ${result.error}")
                    }
                }
            }

            // Actualizar la lista local de saberes
            currentSaberes =
                currentSaberes.map { saber ->
                    saber.copy(completado = _saberesSeleccionados.value.contains(saber.id))
                }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private suspend fun guardarEvaluacionInterno(elemento: Elemento) {
            val elementoActualizado =
                elemento.copy(
                    evaluado = _evaluacionCompletada.value,
                    comentario = _comentario.value,
                    fechaEvaluado = if (_evaluacionCompletada.value) convertirFecha(_evaluacionFecha.value) else null,
                )
            Log.d("elem", elementoActualizado.toString())

            when (val result = updateElemento.invoke(elementoActualizado)) {
                is NetworkResult.Success -> {
                    currentElemento = elementoActualizado

                    val cambioEvaluacion = _evaluacionCompletada.value != originalEvaluacionCompletada
                    if (cambioEvaluacion) {
                        val incrementoEvaluados = if (_evaluacionCompletada.value) 1 else -1
                        Log.d("ViewModel", "Estado evaluación cambió de $originalEvaluacionCompletada a ${_evaluacionCompletada.value}")
                        Log.d("ViewModel", "Actualizando elementos evaluados en materia: $incrementoEvaluados")

                        updateMateria.invoke(
                            elemento.materiaId.toInt(),
                            0,
                            0,
                            incrementoEvaluados,
                            0,
                        )
                    }
                    Log.d("ViewModel", "Evaluación guardada exitosamente")
                }
                is NetworkResult.Error -> {
                    throw Exception("Error al guardar evaluación: ${result.error}")
                }
            }
        }

        private suspend fun guardarRecuperatoriosInterno(elemento: Elemento) {
            var recuperatoriosCreados = 0
            var recuperatoriosEliminados = 0
            var cambioEnRecuperatoriosTomados = 0

            val recuperatoriosNuevos = getRecuperatoriosNuevos()
            for (recuperatorio in recuperatoriosNuevos) {
                when (val result = createRecuperatorio.invoke(recuperatorio)) {
                    is NetworkResult.Success -> {
                        recuperatoriosCreados++

                        if (recuperatorio.completado) {
                            cambioEnRecuperatoriosTomados++
                        }
                        Log.d("ViewModel", "Recuperatorio creado (completado: ${recuperatorio.completado})")
                    }
                    is NetworkResult.Error -> {
                        throw Exception("Error al crear recuperatorio: ${result.error}")
                    }
                }
            }

            val recuperatoriosModificados = getRecuperatoriosModificados()
            for (recuperatorio in recuperatoriosModificados) {
                val original = originalRecuperatorios.find { it.id == recuperatorio.id }

                if (original != null) {
                    when {
                        !original.completado && recuperatorio.completado -> {
                            cambioEnRecuperatoriosTomados++
                            Log.d("ViewModel", "Recuperatorio ${recuperatorio.id} se completó (+1 tomado)")
                        }
                        original.completado && !recuperatorio.completado -> {
                            cambioEnRecuperatoriosTomados--
                            Log.d("ViewModel", "Recuperatorio ${recuperatorio.id} se descompletó (-1 tomado)")
                        }
                    }
                }

                when (val result = updateRecuperatorio.invoke(recuperatorio)) {
                    is NetworkResult.Success -> {
                        Log.d("ViewModel", "Recuperatorio ${recuperatorio.id} actualizado")
                    }
                    is NetworkResult.Error -> {
                        throw Exception("Error al actualizar recuperatorio: ${result.error}")
                    }
                }
            }

            val recuperatoriosEliminados_lista = getRecuperatoriosEliminados()
            for (recuperatorio in recuperatoriosEliminados_lista) {
                when (val result = deleteRecuperatorio.invoke(recuperatorio.id)) {
                    is NetworkResult.Success -> {
                        recuperatoriosEliminados++

                        if (recuperatorio.completado) {
                            cambioEnRecuperatoriosTomados--
                        }
                        Log.d("ViewModel", "Recuperatorio ${recuperatorio.id} eliminado (completado: ${recuperatorio.completado})")
                    }
                    is NetworkResult.Error -> {
                        throw Exception("Error al eliminar recuperatorio: ${result.error}")
                    }
                }
            }

            val cambioRecuperatoriosTotales = recuperatoriosCreados - recuperatoriosEliminados

            if (cambioRecuperatoriosTotales != 0 || cambioEnRecuperatoriosTomados != 0) {
                Log.d("ViewModel", "Actualizando materia - Totales: $cambioRecuperatoriosTotales, Tomados: $cambioEnRecuperatoriosTomados")

                updateMateria.invoke(
                    elemento.materiaId.toInt(),
                    cambioEnRecuperatoriosTomados,
                    0,
                    0,
                    cambioRecuperatoriosTotales,
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private suspend fun actualizarEstadoCompletado(elemento: Elemento) {
            val elementoActual = currentElemento ?: elemento

            val progreso = calcularProgreso()
            val deberiaEstarCompletado = progreso.puedeCompletarse

            Log.d("ViewModel", "=== ACTUALIZANDO ESTADO COMPLETADO ===")
            Log.d("ViewModel", "Estado actual completado: ${elementoActual.completado}")
            Log.d("ViewModel", "Debería estar completado: $deberiaEstarCompletado")
            Log.d("ViewModel", "Original completado: $originalElementoCompletado")

            // CORREGIDO: Siempre actualizar saberesCompletados junto con el estado de completado
            val elementoActualizado =
                elementoActual.copy(
                    completado = deberiaEstarCompletado,
                    saberesCompletados = _saberesSeleccionados.value.size, // CORREGIDO: Siempre actualizar
                    fechaRegistro =
                        if (deberiaEstarCompletado && !elementoActual.completado) {
                            LocalDate
                                .now(
                                    ZoneId.of("America/La_Paz"),
                                ).toString()
                        } else {
                            elementoActual.fechaRegistro
                        },
                )

            when (val result = updateElemento.invoke(elementoActualizado)) {
                is NetworkResult.Success -> {
                    currentElemento = elementoActualizado

                    val incrementoCompletados =
                        when {
                            deberiaEstarCompletado && !elementoActual.completado -> 1
                            !deberiaEstarCompletado && elementoActual.completado -> -1
                            else -> 0
                        }

                    if (incrementoCompletados != 0) {
                        Log.d("ViewModel", "Actualizando elementos completados en materia: $incrementoCompletados")
                        updateMateria.invoke(
                            elemento.materiaId.toInt(),
                            0,
                            incrementoCompletados,
                            0,
                            0,
                        )
                    }

                    Log.d("ViewModel", "Estado de completado actualizado: ${elementoActual.completado} -> $deberiaEstarCompletado")
                    Log.d("ViewModel", "Saberes completados actualizado a: ${_saberesSeleccionados.value.size}")
                }
                is NetworkResult.Error -> {
                    Log.w("ViewModel", "Error al actualizar estado completado (no crítico): ${result.error}")
                }
            }
        }

        private fun actualizarEstadosOriginales() {
            originalSaberesSeleccionados = _saberesSeleccionados.value
            originalSaberes = currentSaberes
            originalRecuperatorios = currentRecuperatorios.toList()
            originalEvaluacionCompletada = _evaluacionCompletada.value
            originalComentario = _comentario.value

            originalElementoCompletado = currentElemento?.completado ?: false
        }

        private fun getSaberesModificados(): List<Pair<Int, Boolean>> {
            val cambios = mutableListOf<Pair<Int, Boolean>>()

            val nuevosSeleccionados = _saberesSeleccionados.value - originalSaberesSeleccionados
            cambios.addAll(nuevosSeleccionados.map { it to true })

            val deseleccionados = originalSaberesSeleccionados - _saberesSeleccionados.value
            cambios.addAll(deseleccionados.map { it to false })

            return cambios
        }

        private fun getRecuperatoriosModificados(): List<Recuperatorio> =
            currentRecuperatorios.filter { current ->
                current.id != 0 &&
                    originalRecuperatorios.find { it.id == current.id }?.let { original ->
                        original.completado != current.completado ||
                            original.fechaEvaluado != current.fechaEvaluado
                    } ?: false
            }

        private fun getRecuperatoriosNuevos(): List<Recuperatorio> = currentRecuperatorios.filter { it.id == 0 }

        private fun getRecuperatoriosEliminados(): List<Recuperatorio> =
            originalRecuperatorios.filter { original ->
                currentRecuperatorios.none { it.id == original.id }
            }

        fun updateRecuperatorioLocal(
            index: Int,
            completado: Boolean? = null,
            fecha: String? = null,
        ) {
            val fechaFormateada = fecha?.let { convertirFecha(it) }
            if (index < currentRecuperatorios.size) {
                val recuperatorio = currentRecuperatorios[index]
                currentRecuperatorios[index] =
                    recuperatorio.copy(
                        completado = completado ?: recuperatorio.completado,
                        fechaEvaluado = fechaFormateada ?: recuperatorio.fechaEvaluado,
                    )
                updateUIState()
            }
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
            updateUIState()
        }

        fun eliminarRecuperatorio(index: Int) {
            if (currentRecuperatorios.size > 1 && index < currentRecuperatorios.size) {
                currentRecuperatorios.removeAt(index)
                updateUIState()
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
            updateUIState()
        }

        fun setEvaluacionFecha(fecha: String) {
            _evaluacionFecha.value = fecha
        }

        fun setEvaluacionCompletada(completada: Boolean) {
            _evaluacionCompletada.value = completada
            updateUIState()
        }

        fun setComentario(comentario: String) {
            _comentario.value = comentario
        }

        fun convertirFecha(fechaUI: String): String =
            fechaUI.substring(0, 10).split("-").let { partes ->
                if (partes.size != 3) fechaUI else "${partes[2]}-${partes[1]}-${partes[0]}"
            }

        private fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
            return dateFormat.format(Date())
        }

        fun debugProgreso() {
            val progreso = calcularProgreso()
            Log.d("ViewModel", "=== DEBUG PROGRESO ===")
            Log.d("ViewModel", "Tiene saberes: ${progreso.tieneSaberes}")
            Log.d("ViewModel", "Está evaluado: ${progreso.estaEvaluado}")
            Log.d("ViewModel", "Tiene recuperatorios: ${progreso.tieneRecuperatorios}")
            Log.d("ViewModel", "Puede completarse: ${progreso.puedeCompletarse}")
            Log.d("ViewModel", "Hay pendientes: ${progreso.hayPendientes}")
            Log.d("ViewModel", "Saberes seleccionados: ${_saberesSeleccionados.value}")
            Log.d("ViewModel", "Saberes originales: $originalSaberesSeleccionados")
            Log.d("ViewModel", "Evaluación actual: ${_evaluacionCompletada.value}")
            Log.d("ViewModel", "Evaluación original: $originalEvaluacionCompletada")
            Log.d("ViewModel", "Elemento completado actual: ${currentElemento?.completado}")
            Log.d("ViewModel", "Elemento completado original: $originalElementoCompletado")
            Log.d("ViewModel", "=====================")
        }
    }
