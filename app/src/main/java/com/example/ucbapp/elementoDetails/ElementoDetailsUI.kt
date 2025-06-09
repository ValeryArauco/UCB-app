package com.example.ucbapp.elementoDetails
import TopAppBarSection
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.Elemento
import com.example.domain.Recuperatorio
import com.example.domain.Saber
import com.example.ucbapp.materias.ErrorView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementoDetailsUI(
    elemento: Elemento,
    onBackPressed: () -> Unit,
    elementoDetailsViewModel: ElementoDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        elementoDetailsViewModel.loadData(elemento)
    }
    val elementoState by elementoDetailsViewModel.uiState.collectAsState()
    val saberesSeleccionados by elementoDetailsViewModel.saberesSeleccionados.collectAsState()
    val evaluacionFecha by elementoDetailsViewModel.evaluacionFecha.collectAsState()
    val evaluacionCompletada by elementoDetailsViewModel.evaluacionCompletada.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val comentario by elementoDetailsViewModel.comentario.collectAsState()
    val context = LocalContext.current
    val datePickerState =
        rememberDatePickerState(
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= System.currentTimeMillis()
                },
        )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA),
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(paddingValues),
        ) {
            TopAppBarSection(
                "ELEMENTO DE COMPETENCIA ${elemento.descripcion[0]}",
                onBackPressed,
            )

            when (val ui = elementoState) {
                is ElementoDetailsViewModel.UIState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cargando...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                is ElementoDetailsViewModel.UIState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        item {
                            SectionCard(title = "Saberes mínimos") {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    ui.saberes.forEach { saber ->
                                        SaberItem(
                                            item = saber,
                                            isSelected = saberesSeleccionados.contains(saber.id),
                                            onSelectionChanged = { selected ->
                                                elementoDetailsViewModel.toggleSaber(saber.id, selected)
                                            },
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            SectionCard(title = "Confirmar evaluación") {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    if (!evaluacionCompletada) {
                                        Button(
                                            onClick = {
                                                showDatePicker = true
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors =
                                                ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary,
                                                ),
                                        ) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                                            Spacer(Modifier.width(8.dp))
                                            Text("Marcar como evaluado")
                                        }
                                    } else {
                                        Row(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        Color(0xFFE8F5E8),
                                                        RoundedCornerShape(8.dp),
                                                    ).padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Column {
                                                Text(
                                                    "Evaluado",
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF2E7D32),
                                                )
                                                Text(
                                                    "Fecha: $evaluacionFecha",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF558B2F),
                                                )
                                            }
                                            Spacer(modifier = Modifier.weight(1f))

                                            TextButton(onClick = {
                                                showDatePicker = true
                                            }) {
                                                Text("Cambiar", fontSize = 12.sp)
                                            }
                                            TextButton(
                                                onClick = { elementoDetailsViewModel.setEvaluacionCompletada(false) },
                                            ) {
                                                Text(
                                                    "Deshacer",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF757575),
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            if (showDatePicker) {
                                DatePickerDialog(
                                    onDismissRequest = { showDatePicker = false },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                val selectedDateMillis = datePickerState.selectedDateMillis
                                                if (selectedDateMillis != null) {
                                                    val selectedDate = Date(selectedDateMillis)
                                                    val formattedDate = formatDateToString(selectedDate)
                                                    elementoDetailsViewModel.setEvaluacionFecha(formattedDate)
                                                    elementoDetailsViewModel.setEvaluacionCompletada(true)
                                                }
                                                showDatePicker = false
                                            },
                                        ) {
                                            Text(text = "Aceptar")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = { showDatePicker = false },
                                        ) {
                                            Text(text = "Cancelar")
                                        }
                                    },
                                ) {
                                    DatePicker(state = datePickerState)
                                }
                            }
                        }
                        item {
                            SectionCard(title = "Recuperatorios") {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    ui.recuperatorios.forEachIndexed { index, recuperatorio ->
                                        RecuperatorioCard(
                                            recuperatorio = recuperatorio,
                                            numeroRecuperatorio = index + 1,
                                            puedeEliminar = ui.recuperatorios.size > 1,
                                            onMarcarCompletado = { fecha ->
                                                elementoDetailsViewModel.updateRecuperatorioLocal(index, true, fecha)
                                            },
                                            onDesmarcar = {
                                                elementoDetailsViewModel.updateRecuperatorioLocal(index, false, null)
                                            },
                                            onCambiarFecha = { fecha ->
                                                elementoDetailsViewModel.updateRecuperatorioLocal(index, null, fecha)
                                            },
                                            onEliminar = {
                                                if (elementoDetailsViewModel.currentRecuperatorios.size == 1) {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "No se puede eliminar: cada elemento debe tener al menos un recuperatorio",
                                                            Toast.LENGTH_SHORT,
                                                        ).show()
                                                } else {
                                                    elementoDetailsViewModel.eliminarRecuperatorio(index)
                                                }
                                            },
                                        )
                                    }

                                    TextButton(
                                        onClick = { elementoDetailsViewModel.agregarRecuperatorio(elemento) },
                                        modifier = Modifier.align(Alignment.End),
                                    ) {
                                        Text(
                                            text = "Agregar",
                                            color = Color(0xFF1976D2),
                                            fontSize = 14.sp,
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            SectionCard(title = "Comentarios") {
                                OutlinedTextField(
                                    value = comentario,
                                    onValueChange = { elementoDetailsViewModel.setComentario(it) },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text(
                                            text = "Añadir comentario (Opcional)",
                                            color = Color.Gray,
                                        )
                                    },
                                    minLines = 3,
                                    colors =
                                        OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF1976D2),
                                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                                        ),
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showConfirmDialog = true },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                    ),
                                shape = RoundedCornerShape(18.dp),
                            ) {
                                Text(
                                    text = "Completar elemento",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                            if (showConfirmDialog) {
                                ConfirmarRegistroDialog(
                                    onDismissRequest = { showConfirmDialog = false },
                                    onConfirmation = {
                                        elementoDetailsViewModel.completarElemento()
                                        showConfirmDialog = false
                                        onBackPressed()
                                    },
                                )
                            }
                        }
                    }
                }

                is ElementoDetailsViewModel.UIState.Error -> {
                    ErrorView(
                        message = ui.message,
                        onRetry = { elementoDetailsViewModel.loadData(elemento) },
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmarRegistroDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    var confirmacion by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Confirmación de cambios")
        },
        text = {
            Column {
                Text(
                    text = "Escribe \"Confirmar\" para guardar los cambios de forma permanente. Ten en cuenta que no podrás modificarlos después.",
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                OutlinedTextField(
                    value = confirmacion,
                    onValueChange = { confirmacion = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Confirmar",
                            color = Color.Gray,
                        )
                    },
                    singleLine = true,
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1976D2),
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        ),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (confirmacion.trim().equals("Confirmar", ignoreCase = false)) {
                        onConfirmation()
                    } else {
                        Toast
                            .makeText(
                                context,
                                "Error al escribir 'Confirmar', por favor inténtelo nuevamente",
                                Toast.LENGTH_SHORT,
                            ).show()
                    }
                },
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text("Cancelar")
            }
        },
    )
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            content()
        }
    }
}

@Composable
fun SaberItem(
    item: Saber,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.descripcion,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f),
        )
        Checkbox(
            checked = isSelected,
            onCheckedChange = onSelectionChanged,
            colors =
                CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = Color.Gray,
                ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    fecha: String,
    onFechaChanged: (String) -> Unit,
    label: String,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState =
        rememberDatePickerState(
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= System.currentTimeMillis()
                },
        )

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
                .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Seleccionar fecha",
            tint = Color(0xFF1976D2),
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = fecha,
            fontSize = 14.sp,
            color = Color.Black,
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val selectedDate = Date(selectedDateMillis)
                            val formattedDate = formatDateToString(selectedDate)
                            onFechaChanged(formattedDate)
                        }
                        showDatePicker = false
                    },
                ) {
                    Text(text = "Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                ) {
                    Text(text = "Cancelar")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

fun formatDateToString(date: Date): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(date)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperatorioCard(
    recuperatorio: Recuperatorio,
    numeroRecuperatorio: Int,
    puedeEliminar: Boolean,
    onMarcarCompletado: (String) -> Unit,
    onDesmarcar: () -> Unit,
    onCambiarFecha: (String) -> Unit,
    onEliminar: () -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val datePickerState =
        rememberDatePickerState(
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= System.currentTimeMillis()
                },
        )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (recuperatorio.completado) {
                        Color(0xFFE8F5E8)
                    } else {
                        Color.White
                    },
            ),
        border =
            BorderStroke(
                if (recuperatorio.completado) 0.dp else 1.dp,
                Color(0xFFE0E0E0),
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    IconButton(
                        onClick = {
                            if (recuperatorio.completado) {
                                onDesmarcar()
                            } else {
                                showDatePicker = true
                            }
                        },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            if (recuperatorio.completado) {
                                Icons.Default.CheckCircle
                            } else {
                                Icons.Outlined.CheckCircle
                            },
                            contentDescription = null,
                            tint =
                                if (recuperatorio.completado) {
                                    Color(0xFF4CAF50)
                                } else {
                                    Color(0xFF757575)
                                },
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            "$numeroRecuperatorio° Recuperatorio",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color =
                                if (recuperatorio.completado) {
                                    Color(0xFF2E7D32)
                                } else {
                                    Color.Black
                                },
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        if (!recuperatorio.completado) {
                            Text(
                                "Pendiente de realizar",
                                fontSize = 14.sp,
                                color = Color(0xFF757575),
                                style = TextStyle(fontStyle = FontStyle.Italic),
                            )
                        } else {
                            Row {
                                Text(
                                    "Realizado - ",
                                    fontSize = 12.sp,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Medium,
                                )

                                Text(
                                    "Fecha: ${recuperatorio.fechaEvaluado}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF558B2F),
                                )
                            }
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    Box {
                        IconButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.size(32.dp),
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Más opciones",
                                tint = Color(0xFF757575),
                                modifier = Modifier.size(18.dp),
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            if (recuperatorio.completado) {
                                DropdownMenuItem(
                                    text = { Text("Cambiar fecha") },
                                    onClick = {
                                        showDatePicker = true
                                        expanded = false
                                    },
                                )
                            }

                            DropdownMenuItem(
                                text = { Text("Eliminar") },
                                onClick = {
                                    showDeleteDialog = true
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val selectedDate = Date(selectedDateMillis)
                            val formattedDate = formatDateToString(selectedDate)
                            if (recuperatorio.completado) {
                                onCambiarFecha(formattedDate)
                            } else {
                                onMarcarCompletado(formattedDate)
                            }
                        }
                        showDatePicker = false
                    },
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar recuperatorio") },
            text = {
                Text("¿Estás seguro de que deseas eliminar este recuperatorio?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminar()
                        showDeleteDialog = false
                    },
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            },
        )
    }
}
