package com.medicat.ucbapp.elementoDetails
import TopAppBarSection
import android.os.Build
import android.util.Log
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
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
import com.medicat.domain.Elemento
import com.medicat.domain.Recuperatorio
import com.medicat.domain.Saber
import com.medicat.ucbapp.materias.ErrorView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)),
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
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                if (!evaluacionCompletada) {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors =
                                            CardDefaults.cardColors(
                                                containerColor = Color(0xFFF3F4F6),
                                            ),
                                        border = BorderStroke(2.dp, Color(0xFFE5E7EB)),
                                        shape = RoundedCornerShape(12.dp),
                                    ) {
                                        Column(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .clickable { showDatePicker = true }
                                                    .padding(20.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                modifier = Modifier.size(32.dp),
                                                tint = MaterialTheme.colorScheme.primary,
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Marcar como evaluado",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.primary,
                                            )
                                            Text(
                                                text = "Toca para seleccionar fecha",
                                                fontSize = 12.sp,
                                                color = Color(0xFF6B7280),
                                                modifier = Modifier.padding(top = 2.dp),
                                            )
                                        }
                                    }
                                } else {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors =
                                            CardDefaults.cardColors(
                                                containerColor = Color(0xFFE8F5E8),
                                            ),
                                        shape = RoundedCornerShape(12.dp),
                                    ) {
                                        Column(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(20.dp),
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Icon(
                                                    Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(28.dp),
                                                    tint = Color(0xFF4CAF50),
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = "Evaluación completada",
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = Color(0xFF2E7D32),
                                                    )
                                                    Text(
                                                        text = "Fecha: ${formatDateForDisplay(evaluacionFecha)}",
                                                        fontSize = 13.sp,
                                                        color = Color(0xFF2E7D32),
                                                        modifier = Modifier.padding(top = 2.dp),
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(12.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End,
                                            ) {
                                                TextButton(
                                                    onClick = { showDatePicker = true },
                                                    colors =
                                                        ButtonDefaults.textButtonColors(
                                                            contentColor = Color(0xFF059669),
                                                        ),
                                                ) {
                                                    Icon(
                                                        Icons.Default.Edit,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(14.dp),
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Cambiar fecha", fontSize = 12.sp)
                                                }

                                                TextButton(
                                                    onClick = { elementoDetailsViewModel.setEvaluacionCompletada(false) },
                                                    colors =
                                                        ButtonDefaults.textButtonColors(
                                                            contentColor = Color(0xFF6B7280),
                                                        ),
                                                ) {
                                                    Icon(
                                                        Icons.Default.Close,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(14.dp),
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Deshacer", fontSize = 12.sp)
                                                }
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
                                                val selectedDate = Date(selectedDateMillis + 86400000)
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
                                text = "Guardar Cambios",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                        if (showConfirmDialog) {
                            ConfirmarRegistroDialog(
                                onDismissRequest = { showConfirmDialog = false },
                                onConfirmation = {
                                    showConfirmDialog = false
                                    onBackPressed()
                                },
                                elementoDetailsViewModel = elementoDetailsViewModel,
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmarRegistroDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    elementoDetailsViewModel: ElementoDetailsViewModel,
) {
    var confirmacion by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest =
            if (isProcessing) {
                {}
            } else {
                onDismissRequest
            },
        title = {
            Text(text = "Confirmación de cambios")
        },
        text = {
            Column {
                Text(
                    text = "Escribe \"Confirmar\" para guardar los cambios. La información registrada será visible en tu progreso y se incluirá en los reportes para la administración.",
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                OutlinedTextField(
                    value = confirmacion,
                    onValueChange = { confirmacion = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing,
                    placeholder = {
                        Text(
                            text = "Confirmar",
                            color = Color.Gray,
                        )
                    },
                    singleLine = true,
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        ),
                )

                if (isProcessing) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Guardando cambios...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (confirmacion.trim().equals("Confirmar", ignoreCase = false)) {
                        isProcessing = true
                        scope.launch {
                            try {
                                elementoDetailsViewModel.guardarCambios().join()

                                isProcessing = false
                                onConfirmation()
                            } catch (e: Exception) {
                                isProcessing = false
                                Toast
                                    .makeText(
                                        context,
                                        "Error al guardar: ${e.message}",
                                        Toast.LENGTH_LONG,
                                    ).show()
                            }
                        }
                    } else {
                        Toast
                            .makeText(
                                context,
                                "Error al escribir 'Confirmar', por favor inténtelo nuevamente",
                                Toast.LENGTH_SHORT,
                            ).show()
                    }
                },
                enabled = !isProcessing,
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                enabled = !isProcessing,
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

fun formatDateToString(date: Date): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(date)
}

fun formatDateForDisplay(dateString: String): String {
    Log.d("display date", "before $dateString")
    if (dateString.isEmpty()) return ""

    try {
        // Limpiar la string de caracteres extra
        val cleanDate = dateString.trim()

        // Caso 1: yyyy-MM-dd (ISO format)
        if (cleanDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            val parts = cleanDate.split("-")
            val year = parts[0]
            val month = parts[1]
            val day = parts[2]
            val result = "$day-$month-$year"
            Log.d("display date", "converted yyyy-MM-dd to $result")
            return result
        }

        // Caso 2: yyyy-MM-ddTHH:mm:ss.SSSZ (ISO with time)
        if (cleanDate.contains("T") && (cleanDate.contains("Z") || cleanDate.contains("+"))) {
            val datePart = cleanDate.split("T")[0]
            if (datePart.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                val parts = datePart.split("-")
                val year = parts[0]
                val month = parts[1]
                val day = parts[2]
                val result = "$day-$month-$year"
                Log.d("display date", "converted ISO timestamp to $result")
                return result
            }
        }

        // Caso 3: dd-MM-yyyy (ya está en el formato correcto)
        if (cleanDate.matches(Regex("\\d{2}-\\d{2}-\\d{4}"))) {
            Log.d("display date", "already in correct format")
            return cleanDate
        }

        // Caso 4: dd/MM/yyyy
        if (cleanDate.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
            val result = cleanDate.replace("/", "-")
            Log.d("display date", "converted dd/MM/yyyy to $result")
            return result
        }

        Log.d("display date", "no pattern matched, returning original")
        return dateString
    } catch (e: Exception) {
        Log.e("display date", "error processing date: ${e.message}")
        return dateString
    }
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
                                    "Realizado: ",
                                    fontSize = 12.sp,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Medium,
                                )

                                Text(
                                    formatDateForDisplay(recuperatorio.fechaEvaluado ?: ""),
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
                            val selectedDate = Date(selectedDateMillis + 86400000)
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
