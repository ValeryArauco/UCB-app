package com.medicat.ucbapp.navigation

sealed class Screens(
    val route: String,
) {
    object LoginScreen : Screens("login")

    object MateriasScreen : Screens("misMaterias")

    object ElementosScreen : Screens("elementos")

    object ElementoDetailsScreen : Screens("elementoDetails")

    object NotificationsScreen : Screens("notifications")

    object PerfilScreen : Screens("perfil")
}
