package com.example.ucbapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NotificationsScreen() {
    Text(text = "Pantalla de Notificaciones")
}

@Composable
fun MessagesScreen() {
    Text(text = "Pantalla de Mensajes")
}

@Composable
fun LoginScreen() {
    Text(text = "Pantalla de Inicio de Sesión")
}

@Preview
@Composable
fun PreviewNotificationsScreen() {
    NotificationsScreen()
}