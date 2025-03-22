package com.uob.framework.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.uob.framework.ui.UserMenu
import com.uob.framework.ui.NotificationsScreen
import com.uob.framework.ui.MessagesScreen
import com.uob.framework.ui.LoginScreen

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