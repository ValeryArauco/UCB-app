package com.example.ucbapp.ui.screens

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

@Composable
fun UserMenu(navController: NavController, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }  // Controla si el menú está expandido
    var isLightMode by remember { mutableStateOf(true) }  // Controla el modo claro/oscuro

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Botón para abrir el menú desplegable
        IconButton(
            onClick = { expanded = true },  // Abre el menú al hacer clic
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,  // Ícono de menú
                contentDescription = "Abrir menú"
            )
        }

        // Menú desplegable
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },  // Cierra el menú al tocar fuera
            modifier = Modifier.width(200.dp)
        ) {
            // Información del usuario
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

            // Opciones del menú
            DropdownMenuItem(
                text = { Text("Notificaciones") },
                onClick = {
                    expanded = false  // Cierra el menú
                    navController.navigate("notifications")  // Navegar a notificaciones
                }
            )
            DropdownMenuItem(
                text = { Text("Mensajes") },
                onClick = {
                    expanded = false  // Cierra el menú
                    navController.navigate("messages")  // Navegar a mensajes
                }
            )
            DropdownMenuItem(
                text = { Text(if (isLightMode) "Dark Mode" else "Light Mode") },
                onClick = {
                    expanded = false  // Cierra el menú
                    isLightMode = !isLightMode  // Cambia entre modo claro y oscuro
                    // Aquí puedes cambiar el tema de la aplicación
                    println("Cambiar a ${if (isLightMode) "Light Mode" else "Dark Mode"}")
                }
            )
            DropdownMenuItem(
                text = { Text("Cerrar sesión") },
                onClick = {
                    expanded = false  // Cierra el menú
                    navController.navigate("login") {  // Navegar a login
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}