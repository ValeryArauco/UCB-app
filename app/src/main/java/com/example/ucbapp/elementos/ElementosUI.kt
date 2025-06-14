@file:OptIn(ExperimentalMaterial3Api::class)

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.Elemento
import com.example.domain.Materia
import com.example.ucbapp.dashboard.CompetencyCard
import com.example.ucbapp.elementos.ElementosViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ElementosUI(
    materia: Materia,
    onBackPressed: () -> Unit,
    onClick: (Elemento) -> Unit,
    elementosViewModel: ElementosViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        elementosViewModel.loadElementos(materia)
    }

    LaunchedEffect(materia.elemCompletados, materia.elemEvaluados, materia.recTomados) {
        elementosViewModel.refreshMateriaData(materia)
    }
    val elementosState by elementosViewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf(FilterType.ALL) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)),
        ) {
            TopAppBarSection(
                materia.name,
                onBackPressed,
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
            ) {
                Column {
                    val updatedMateria =
                        when (val state = elementosState) {
                            is ElementosViewModel.ElementosUIState.Loaded -> state.materia ?: materia
                            else -> materia
                        }
                    ProgressSection(updatedMateria)

                    FilterChips(
                        selectedFilter = selectedFilter,
                        onFilterSelected = { selectedFilter = it },
                    )
                }
            }
            when (val ui = elementosState) {
                is ElementosViewModel.ElementosUIState.Loading -> {
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
                                text = "Cargando elementos...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                is ElementosViewModel.ElementosUIState.Loaded -> {
                    val filteredElementos = filterElementos(ui.elementos, selectedFilter)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(filteredElementos) { elemento ->
                            CompetencyCard(
                                element = elemento,
                                onClick = { onClick(elemento) },
                            )
                        }
                    }
                }

                is ElementosViewModel.ElementosUIState.Error -> {
                    ErrorView(
                        message = ui.message,
                        onRetry = { elementosViewModel.loadElementos(materia) },
                    )
                }
            }
        }
    }
}

@Composable
fun TopAppBarSection(
    title: String,
    onBackClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        windowInsets = WindowInsets(0.dp),
        modifier = Modifier.statusBarsPadding(),
    )
}

@Composable
fun ProgressSection(materia: Materia) {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Text(
            text = "Progreso de la materia",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.padding(bottom = 12.dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Completos",
                        fontSize = 11.sp,
                        color = Color(0xFF666666),
                    )
                    Text(
                        text = "${materia.elemCompletados}/${materia.elementosTotales}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                LinearProgressIndicator(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                    progress = { (materia.elemCompletados / materia.elementosTotales.toFloat()) },
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color(0xFFF5F5F5),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Evaluados",
                        fontSize = 11.sp,
                        color = Color(0xFF666666),
                    )
                    Text(
                        text = "${materia.elemEvaluados}/${materia.elementosTotales}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC107),
                    )
                }
                LinearProgressIndicator(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                    progress = { (materia.elemEvaluados / materia.elementosTotales.toFloat()) },
                    color = Color(0xFFFFC107),
                    trackColor = Color(0xFFF5F5F5),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Recup.",
                        fontSize = 11.sp,
                        color = Color(0xFF666666),
                    )
                    Text(
                        text = "${materia.recTomados}/${materia.recTotales}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9E9E9E),
                    )
                }
                LinearProgressIndicator(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                    progress = { (materia.recTomados / materia.recTotales.toFloat()) },
                    color = Color(0xFF9E9E9E),
                    trackColor = Color(0xFFF5F5F5),
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompetencyCard(
    element: Elemento,
    onClick: (Elemento) -> Unit,
) {
    val (numeroElemento, descripcionLimpia) = extraerNumeroYDescripcion(element.descripcion)
    val hoy = LocalDate.now()
    val fechaLimite = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(element.fechaLimite)).atZone(ZoneId.systemDefault()).toLocalDate()
    val diasRestantes = ChronoUnit.DAYS.between(hoy, fechaLimite)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.size(62.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        progress = { (element.saberesCompletados / element.saberesTotales.toFloat()) },
                        modifier = Modifier.size(62.dp),
                        color =
                            if (element.saberesCompletados == element.saberesTotales) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color(0xFFFFC107)
                            },
                        strokeWidth = 6.dp,
                        trackColor = Color(0xFFF5F5F5),
                        strokeCap = StrokeCap.Round,
                    )
                    Text(
                        text = "${(element.saberesCompletados / element.saberesTotales.toFloat() * 100).toInt()}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = "ELEMENTO DE COMPETENCIA $numeroElemento",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.sp,
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = descripcionLimpia,
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFE8E8E8),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "VENCIMIENTO",
                        fontSize = 10.sp,
                        color = Color(0xFF999999),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                    )
                    Row {
                        Text(
                            text = formatearFecha(element.fechaLimite),
                            fontSize = 12.sp,
                            color =
                                when {
                                    element.completado -> Color(0xFF333333)
                                    diasRestantes < 0 -> Color.Red
                                    diasRestantes <= 7 -> Color(0xFF000000)
                                    else -> Color(0xFF000000)
                                },
                            fontWeight = FontWeight.Medium,
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        if (!element.completado && diasRestantes < 7) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint =
                                    when {
                                        diasRestantes < 0 -> Color.Red
                                        else -> Color(0xFFFFC107)
                                    },
                            )
                        }
                    }
                }

                if (element.completado) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "COMPLETADO",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp,
                        )
                        Text(
                            text = formatearFecha(element.fechaRegistro ?: ""),
                            fontSize = 12.sp,
                            color = Color(0xFF333333),
                            fontWeight = FontWeight.Medium,
                        )
                    }
                } else {
                    Button(
                        onClick = { onClick(element) },
                        shape = RoundedCornerShape(24.dp),
                        colors =
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White,
                            ),
                    ) {
                        Text(text = "Registrar")
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChips(
    selectedFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(FilterType.entries) { filter ->
            FilterChip(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filter.displayName,
                        fontWeight = if (selectedFilter == filter) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
                selected = selectedFilter == filter,
                leadingIcon = {
                    when (filter) {
                        FilterType.ALL ->
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                        FilterType.IN_PROGRESS ->
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                        FilterType.COMPLETED ->
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                    }
                },
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
            )
        }
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
        ) {
            Text("Reintentar")
        }
    }
}

fun extraerNumeroYDescripcion(descripcionCompleta: String): Pair<String, String> {
    val regex = """^(\d+)\.(.+)$""".toRegex()
    val matchResult = regex.find(descripcionCompleta.trim())

    return if (matchResult != null) {
        val numero = matchResult.groupValues[1]
        val descripcion = matchResult.groupValues[2].trim()
        Pair(numero, descripcion)
    } else {
        Pair("1", descripcionCompleta)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatearFecha(fechaISO: String): String {
    val fechaLimpia = fechaISO.replace("Z", "")
    val fechaDateTime = LocalDateTime.parse(fechaLimpia, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("es", "ES"))
    return fechaDateTime.format(formatter)
}

fun filterElementos(
    elementos: List<Elemento>,
    filterType: FilterType,
): List<Elemento> =
    elementos.filter { elemento ->

        when (filterType) {
            FilterType.ALL -> true
            FilterType.IN_PROGRESS -> !elemento.completado
            FilterType.COMPLETED -> elemento.completado
        }
    }

enum class FilterType(
    val displayName: String,
) {
    ALL("Todas"),
    IN_PROGRESS("Pendientes"),
    COMPLETED("Completadas"),
}
