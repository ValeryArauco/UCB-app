package com.medicat.ucbapp.notificaciones

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicat.domain.NotificacionItem
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Suppress("ktlint:standard:function-naming")
@Composable
fun NotificacionesUI(viewModel: NotificacionesViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNotificaciones()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA),
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            Text(
                text = "Notificaciones",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            when (val state = uiState) {
                is NotificacionesViewModel.NotificacionesUIState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is NotificacionesViewModel.NotificacionesUIState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Error al cargar notificaciones",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                            )
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                            Button(
                                onClick = { viewModel.loadNotificaciones() },
                                modifier = Modifier.padding(top = 8.dp),
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                is NotificacionesViewModel.NotificacionesUIState.Loaded -> {
                    if (state.notificaciones.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "No tienes notificaciones",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray,
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(state.notificaciones) { notification ->
                                NotificationCard(
                                    notification = notification,
                                    onMarkAsRead = { id ->
                                        // Aquí necesitarás agregar estas funciones al ViewModel
                                        viewModel.markAsRead(id)
                                    },
                                    onDelete = { id ->
                                        viewModel.deleteNotification(id)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationCard(
    notification: NotificacionItem,
    onMarkAsRead: (Int) -> Unit,
    onDelete: (Int) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        border =
            BorderStroke(
                1.dp,
                Color(0xFFE0E0E0),
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            // Header con título y indicador
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )

                if (!notification.isRead) {
                    Box(
                        modifier =
                            Modifier
                                .size(10.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape,
                                ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fecha y botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatDate(notification.sentAt), // Función para formatear fecha
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (!notification.isRead) {
                        FilledTonalIconButton(
                            onClick = { onMarkAsRead(notification.id) },
                            modifier = Modifier.size(36.dp),
                            colors =
                                IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    contentColor = MaterialTheme.colorScheme.primary,
                                ),
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Marcar como leída",
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }

                    FilledTonalIconButton(
                        onClick = { onDelete(notification.id) },
                        modifier = Modifier.size(36.dp),
                        colors =
                            IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.error,
                            ),
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        }
    }
}

// Función auxiliar para formatear la fecha
@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateString: String): String =
    try {
        // Aquí puedes implementar el formateo que prefieras
        val instant = Instant.parse(dateString)
        val formatter =
            DateTimeFormatter
                .ofPattern("dd MMM, HH:mm")
                .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        dateString // Devolver original si hay error
    }
