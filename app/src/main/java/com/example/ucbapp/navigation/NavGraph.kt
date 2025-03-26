package com.example.ucbapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ucbapp.ui.screens.LoginScreen
import com.example.ucbapp.ui.screens.MessagesScreen
import com.example.ucbapp.ui.screens.NotificationsScreen
import com.example.ucbapp.ui.screens.UserMenu


@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "userMenu"
    ) {
        composable("userMenu") {
            UserMenu(navController)
        }
        composable("notifications") {
            NotificationsScreen()
        }
        composable("messages") {
            MessagesScreen()
        }
        composable("login") {
            LoginScreen()
        }
    }
}