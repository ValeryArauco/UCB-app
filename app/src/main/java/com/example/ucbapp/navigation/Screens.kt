package com.example.ucbapp.navigation

sealed class Screens(
    val route: String,
) {
    object LoginScreen : Screens("login")

    object MateriasScreen : Screens("misMaterias")

    object MateriaDetailScreen : Screens("materiaDetail")

    object NotificationsScreen : Screens("notifications")

    object PerfilScreen : Screens("perfil")
}
