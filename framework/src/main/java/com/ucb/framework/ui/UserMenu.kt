package com.ucb.framework.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uob.framework.viewmodels.ThemeViewModel

@Composable
fun UserMenu(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var isLightMode by remember { mutableStateOf(true) }
    val viewModel: ThemeViewModel = viewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Abrir menú"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(200.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Valery Fernanda Arauco Porrez",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "valery.arauco@ucb.edu.bo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            DropdownMenuItem(
                text = { Text("Notificaciones") },
                onClick = {
                    expanded = false
                    navController.navigate("notifications")
                }
            )
            DropdownMenuItem(
                text = { Text("Mensajes") },
                onClick = {
                    expanded = false
                    navController.navigate("messages")
                }
            )
            DropdownMenuItem(
                text = { Text(if (isLightMode) "Dark Mode" else "Light Mode") },
                onClick = {
                    expanded = false
                    isLightMode = !isLightMode
                    viewModel.toggleTheme(isLightMode)
                }
            )
            DropdownMenuItem(
                text = { Text("Cerrar sesión") },
                onClick = {
                    expanded = false
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}