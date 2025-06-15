package com.medicat.ucbapp.materias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.medicat.domain.Materia

@Suppress("ktlint:standard:function-naming")
@Composable
fun MateriasUI(
    materiasViewModel: MateriasViewModel = hiltViewModel(),
    onSuccess: (Materia) -> Unit,
) {
    LaunchedEffect(Unit) {
        materiasViewModel.loadMaterias()
    }
    val materiasState by materiasViewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(FilterType.IN_PROGRESS) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                ) {
                    Text(
                        text = "Mis Materias",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SearchBar(
                        searchText = searchText,
                        onSearchTextChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FilterChips(
                        selectedFilter = selectedFilter,
                        onFilterSelected = { selectedFilter = it },
                    )
                }
            }

            when (val ui = materiasState) {
                is MateriasViewModel.MateriasUIState.Loading -> {
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
                                text = "Cargando materias...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                is MateriasViewModel.MateriasUIState.Loaded -> {
                    val filteredMaterias = filterMaterias(ui.materias, searchText, selectedFilter)

                    if (filteredMaterias.isEmpty()) {
                        EmptyStateView(
                            searchText = searchText,
                            selectedFilter = selectedFilter,
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(filteredMaterias) { materia ->
                                MateriaCard(
                                    materia = materia,
                                    onClick = { onSuccess(materia) },
                                )
                            }
                        }
                    }
                }
                is MateriasViewModel.MateriasUIState.Error -> {
                    ErrorView(
                        message = ui.message,
                        onRetry = { materiasViewModel.loadMaterias() },
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = modifier,
        placeholder = {
            Text(
                text = "Buscar materias...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchTextChange("") },
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
        singleLine = true,
    )
}

enum class FilterType(
    val displayName: String,
) {
    IN_PROGRESS("En Progreso"),
    COMPLETED("Pasadas"),
    ALL("Todas"),
}

@Composable
fun FilterChips(
    selectedFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        items(FilterType.entries) { filter ->
            FilterChip(
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
fun EmptyStateView(
    searchText: String,
    selectedFilter: FilterType,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = if (searchText.isNotEmpty()) Icons.Default.Search else Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =
                when {
                    searchText.isNotEmpty() -> "No se encontraron materias"
                    selectedFilter != FilterType.ALL -> "No hay materias ${selectedFilter.displayName.lowercase()}"
                    else -> "No tienes materias registradas"
                },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (searchText.isNotEmpty()) {
            Text(
                text = "Intenta con otros términos de búsqueda",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

fun filterMaterias(
    materias: List<Materia>,
    searchText: String,
    filterType: FilterType,
): List<Materia> =
    materias.filter { materia ->
        val matchesSearch =
            if (searchText.isBlank()) {
                true
            } else {
                materia.name.contains(searchText, ignoreCase = true) ||
                    materia.sigla.contains(searchText, ignoreCase = true) ||
                    materia.paralelo.contains(searchText, ignoreCase = true)
            }

        val matchesFilter =
            when (filterType) {
                FilterType.ALL -> true
                FilterType.IN_PROGRESS -> materia.vigente
                FilterType.COMPLETED -> !materia.vigente
            }

        matchesSearch && matchesFilter
    }

@Suppress("ktlint:standard:function-naming")
@Composable
fun MateriaCard(
    materia: Materia,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(materia.image)
                        .crossfade(true)
                        .build(),
                contentDescription = materia.name,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = materia.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = "[${materia.sigla}]",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Par. ${materia.paralelo}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFE8E8EA),
                            ) {
                                Text(
                                    text = materia.gestion,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Black,
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = onClick,
                        modifier =
                            Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(8.dp),
                                ).size(40.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Ingresar",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
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
