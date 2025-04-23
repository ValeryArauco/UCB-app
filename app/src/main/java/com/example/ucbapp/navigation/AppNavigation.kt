package com.example.ucbapp.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.example.domain.Materia
import com.example.ucbapp.MateriaDetail.MateriaUI
import com.example.ucbapp.login.LoginUI
import com.example.ucbapp.materias.MateriasUI
import com.example.ucbapp.notificaciones.NotificacionesUI
import com.example.ucbapp.perfil.PerfilUI
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
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
    val needsBackButton =
        currentRoute in
            listOf(
                Screens.MateriaDetailScreen.route,
            )

    val showTopBar = currentRoute != Screens.LoginScreen.route

    Scaffold(
        topBar = {
            if (showTopBar) {
                AppTopBar(
                    title = "",
                    showBackButton = needsBackButton,
                    onBackClick = { navController.popBackStack() },
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                )
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.LoginScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            modifier = Modifier.padding(paddingValues),
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
                    navController.navigate("${Screens.MateriaDetailScreen.route}/$encodeMovieJson")
                })
            }
            composable(
                route = "${Screens.MateriaDetailScreen.route}/{materia}",
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

                MateriaUI(materia = materia, onBackPressed = { navController.popBackStack() })
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White,
                    )
                }
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
    )
}

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
