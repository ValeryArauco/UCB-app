package com.example.ucbapp.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.Materia
import com.example.ucbapp.materias.ErrorView
import com.example.ucbapp.materias.MateriaCard
import com.example.ucbapp.materias.MateriasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun notificacionesUI(
    materiasViewModel: MateriasViewModel = hiltViewModel(),
    onSuccess: (Materia) -> Unit,
) {
    LaunchedEffect(Unit) {
        materiasViewModel.loadMaterias()
    }
    val materiasState by materiasViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                title = {
                    Text("UCB app")
                },
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                tonalElevation = 0.dp,
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Inicio",
                        )
                    },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { /* Handle navigation */ },
                    colors =
                        NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = Color.White,
                            indicatorColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            unselectedTextColor = Color.White.copy(alpha = 0.7f),
                        ),
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notificaciones",
                        )
                    },
                    label = { Text("Notificaciones") },
                    selected = true,
                    onClick = { /* Handle navigation */ },
                    colors =
                        NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            unselectedTextColor = Color.White.copy(alpha = 0.7f),
                        ),
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Mi perfil",
                        )
                    },
                    label = { Text("Mi perfil") },
                    selected = false,
                    onClick = { /* Handle navigation */ },
                    colors =
                        NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            unselectedTextColor = Color.White.copy(alpha = 0.7f),
                        ),
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF2F2F2)),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
            ) {
                Text(
                    text = "Mis materias",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                when (val ui = materiasState) {
                    is MateriasViewModel.MateriasUIState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is MateriasViewModel.MateriasUIState.Loaded -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(ui.materias) { materia ->
                                MateriaCard(materia = materia, onClick = { onSuccess(materia) })
                            }
                        }
                    }
                    is MateriasViewModel.MateriasUIState.Error -> {
                        val errorMessage = (materiasState as MateriasViewModel.MateriasUIState.Error).message
                        ErrorView(
                            message = errorMessage,
                            onRetry = { },
                        )
                    }
                }
            }
        }
    }
}
