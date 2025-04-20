package com.example.ucbapp.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ucbapp.login.LoginUI
import com.example.ucbapp.materias.MateriasUI
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(Screens.LoginScreen.route) {
            LoginUI(
                onSuccess = {
                    navController.navigate(Screens.MateriasScreen.route)
                },
            )
        }
        composable(Screens.MateriasScreen.route) {
            MateriasUI(onSuccess = { materia ->
                val movieJson = Json.encodeToString(materia)
                val encodeMovieJson = URLEncoder.encode(movieJson, "UTF-8")
                // navController.navigate("${Screens.MovieDetailScreen.route}/$encodeMovieJson")
            })
        }
    }
}
