package com.medicat.ucbapp.notificaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.medicat.data.NetworkResult
import com.medicat.domain.NotificacionItem
import com.medicat.usecases.DeleteNotification
import com.medicat.usecases.GetNotificaciones
import com.medicat.usecases.MarkAsRead
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificacionesViewModel
    @Inject
    constructor(
        private val getNotificaciones: GetNotificaciones,
        private val markAsRead: MarkAsRead,
        private val deleteNotification: DeleteNotification,
    ) : ViewModel() {
        sealed class NotificacionesUIState {
            object Loading : NotificacionesUIState()

            class Loaded(
                val notificaciones: List<NotificacionItem>,
            ) : NotificacionesUIState()

            class Error(
                val message: String,
            ) : NotificacionesUIState()
        }

        private val _uiState = MutableStateFlow<NotificacionesUIState>(NotificacionesUIState.Loading)
        val uiState: StateFlow<NotificacionesUIState> = _uiState

        fun loadNotificaciones() {
            _uiState.value = NotificacionesUIState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                val email = Firebase.auth.currentUser?.email ?: ""
                val response = getNotificaciones.invoke(email)
                when (val result = response) {
                    is NetworkResult.Error -> {
                        _uiState.value = NotificacionesUIState.Error(result.error)
                    }
                    is NetworkResult.Success -> {
                        _uiState.value = NotificacionesUIState.Loaded(result.data)
                    }
                }
            }
        }

        fun markAsRead(notificationId: Int) {
            viewModelScope.launch {
                val result = markAsRead.invoke(notificationId)
                when (result) {
                    is NetworkResult.Success -> {
                        if (result.data) {
                            val currentState = _uiState.value
                            if (currentState is NotificacionesUIState.Loaded) {
                                val updatedNotifications =
                                    currentState.notificaciones.map { notification ->
                                        if (notification.id == notificationId) {
                                            notification.copy(isRead = true)
                                        } else {
                                            notification
                                        }
                                    }
                                _uiState.value = NotificacionesUIState.Loaded(updatedNotifications)
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        // Opcional: manejar el error, por ejemplo mostrando un mensaje
                        // _uiState.value = NotificacionesUIState.Error(result.error)
                    }
                }
            }
        }

        fun deleteNotification(notificationId: Int) {
            viewModelScope.launch {
                val result = deleteNotification.invoke(notificationId)
                when (result) {
                    is NetworkResult.Success -> {
                        if (result.data) {
                            val currentState = _uiState.value
                            if (currentState is NotificacionesUIState.Loaded) {
                                val filteredNotifications =
                                    currentState.notificaciones.filter {
                                        it.id != notificationId
                                    }
                                _uiState.value = NotificacionesUIState.Loaded(filteredNotifications)
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        // Opcional: manejar el error, por ejemplo mostrando un mensaje
                        // _uiState.value = NotificacionesUIState.Error(result.error)
                    }
                }
            }
        }
    }
