package com.uob.framework.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ucbapp.ui.LoginScreen
import com.example.ucbapp.ui.MessagesScreen
import com.example.ucbapp.ui.NotificationsScreen
import com.example.ucbapp.ui.UserMenu


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