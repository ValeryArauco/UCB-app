package com.example.ucbapp.navigation

sealed class Screens(
    val route: String,
) {
    object LoginScreen : Screens("login")
}
