package com.example.ucbapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun UserMenu() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Bienvenido de nuevo")
        Text(text = "Correo electrónico")
        Text(text = "Contraseña")
        Text(text = "Mis materias")
        Text(text = "[1-2024] SIS-321 SEGURIDAD DE SISTEMAS [Par. 1]")
        Text(text = "[1-2024] ANA-111 ANATOMÍA HUMANA [Par. 3 - A]")
    }
}

@Preview
@Composable
fun PreviewUserMenu() {
    UserMenu()
}