package com.example.ucbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.UcbappTheme
import dagger.hilt.android.AndroidEntryPoint
import io.sentry.Sentry

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UcbappTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "userMenu"
    ) {
        composable("userMenu") {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                //                    SigninPage(modifier = Modifier.padding(innerPadding))
                UserMenu(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
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

@Composable
fun UserMenu(navController: NavHostController, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }  // Controla si el menú está expandido
    var isLightMode by remember { mutableStateOf(true) }  // Controla el modo claro/oscuro

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Botón para abrir el menú desplegable
        IconButton(
            onClick = { expanded = true },  // Abre el menú al hacer clic
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,  // Ícono de menú
                contentDescription = "Abrir menú"
            )
        }

        // Menú desplegable
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },  // Cierra el menú al tocar fuera
            modifier = Modifier.width(200.dp)
        ) {
            // Información del usuario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Valery Fernanda Arauco Porrez",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "valery.arauco@ucb.edu.bo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Opciones del menú
            DropdownMenuItem(
                text = { Text("Notificaciones") },
                onClick = {
                    expanded = false  // Cierra el menú
                    navController.navigate("notifications")  // Navegar a notificaciones
                }
            )
            DropdownMenuItem(
                text = { Text("Mensajes") },
                onClick = {
                    expanded = false  // Cierra el menú
                    navController.navigate("messages")  // Navegar a mensajes
                }
            )
            DropdownMenuItem(
                text = { Text(if (isLightMode) "Dark Mode" else "Light Mode") },
                onClick = {
                    expanded = false  // Cierra el menú
                    isLightMode = !isLightMode  // Cambia entre modo claro y oscuro
                    // Aquí puedes cambiar el tema de la aplicación
                    println("Cambiar a ${if (isLightMode) "Light Mode" else "Dark Mode"}")
                }
            )
            DropdownMenuItem(
                text = { Text("Cerrar sesión") },
                onClick = {
                    expanded = false  // Cierra el menú
                    navController.navigate("login") {  // Navegar a login
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun NotificationsScreen() {
    Text(text = "Pantalla de Notificaciones")
}

@Composable
fun MessagesScreen() {
    Text(text = "Pantalla de Mensajes")
}

@Composable
fun LoginScreen() {
    Text(text = "Pantalla de Inicio de Sesión")
}

@Composable
fun SigninPage(modifier: Modifier = Modifier) {
    var userSignIn by remember { mutableStateOf("") }
    var passwordSignIn by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.signin_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Text(
            text = "Bienvenido de nuevo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Correo electrónico",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = userSignIn,
            onValueChange = { userSignIn = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "Contraseña",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = passwordSignIn,
            onValueChange = { passwordSignIn = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(text = if (passwordVisible) "🔒" else "🔓")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF004787)
            )
        ) {
            Text(
                text = stringResource(R.string.signin_button),
                color = Color.White
            )
        }

        Text(
            text = "Or sign in with",
            modifier = Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.Gray),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Google icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Continue with Google")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Hello $name!"
        )
        Text(
            text = "Texto de ejemplo",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Button(onClick = {
            Sentry.captureException(
                RuntimeException("This app uses Sentry! :)")
            )
        }) {
            Text(text = "Break the world")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UcbappTheme {
        Greeting("Android")
    }
}

