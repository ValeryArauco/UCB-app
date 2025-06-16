package com.medicat.ucbapp.navigation

import ElementosUI
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.medicat.domain.Elemento
import com.medicat.domain.Materia
import com.medicat.ucbapp.elementoDetails.ElementoDetailsUI
import com.medicat.ucbapp.login.LoginUI
import com.medicat.ucbapp.materias.MateriasUI
import com.medicat.ucbapp.notificaciones.NotificacionesUI
import com.medicat.ucbapp.perfil.PerfilUI
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry
            ?.destination
            ?.route
            ?.split("/")
            ?.firstOrNull()

    val showBottomBar =
        currentRoute in
            listOf(
                Screens.MateriasScreen.route,
                Screens.NotificationsScreen.route,
                Screens.PerfilScreen.route,
            )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                )
            }
        },
    ) {
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
                    val materiaJson = Json.encodeToString(materia)
                    val encodeMateriaJson = URLEncoder.encode(materiaJson, "UTF-8")
                    navController.navigate("${Screens.ElementosScreen.route}/$encodeMateriaJson")
                })
            }
            composable(
                route = "${Screens.ElementosScreen.route}/{materia}",
                arguments =
                    listOf(
                        navArgument("materia") {
                            type = NavType.StringType
                        },
                    ),
            ) {
                val materiaJson = it.arguments?.getString("materia") ?: ""
                val materiaDecoded = URLDecoder.decode(materiaJson, "UTF-8")
                val materia = Json.decodeFromString<Materia>(materiaDecoded)

                ElementosUI(materia = materia, onBackPressed = {
                    navController.popBackStack()
                }, onClick = { elemento ->
                    val elementoJson = Json.encodeToString(elemento)
                    val encodeElementoJson = URLEncoder.encode(elementoJson, "UTF-8")
                    navController.navigate("${Screens.ElementoDetailsScreen.route}/$encodeElementoJson")
                })
            }
            composable(
                route = "${Screens.ElementoDetailsScreen.route}/{elemento}",
                arguments =
                    listOf(
                        navArgument("elemento") {
                            type = NavType.StringType
                        },
                    ),
            ) {
                val elementoJson = it.arguments?.getString("elemento") ?: ""
                val elementoDecoded = URLDecoder.decode(elementoJson, "UTF-8")
                val elemento = Json.decodeFromString<Elemento>(elementoDecoded)

                ElementoDetailsUI(elemento = elemento, onBackPressed = { navController.popBackStack() })
            }
            composable(Screens.NotificationsScreen.route) {
                NotificacionesUI()
            }
            composable(Screens.PerfilScreen.route) {
                PerfilUI()
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        tonalElevation = 0.dp,
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Inicio",
                )
            },
            label = { Text("Inicio") },
            selected = currentRoute == Screens.MateriasScreen.route,
            onClick = {
                if (currentRoute != Screens.MateriasScreen.route) {
                    navController.navigate(Screens.MateriasScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors =
                NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                ),
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                )
            },
            label = { Text("Notificaciones") },
            selected = currentRoute == Screens.NotificationsScreen.route,
            onClick = {
                if (currentRoute != Screens.NotificationsScreen.route) {
                    navController.navigate(Screens.NotificationsScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors =
                NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                ),
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Mi perfil",
                )
            },
            label = { Text("Mi perfil") },
            selected = currentRoute == Screens.PerfilScreen.route,
            onClick = {
                if (currentRoute != Screens.PerfilScreen.route) {
                    navController.navigate(Screens.PerfilScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors =
                NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                ),
        )
    }
}
