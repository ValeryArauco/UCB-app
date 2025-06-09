package com.example.ucbapp.perfil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Suppress("ktlint:standard:function-naming")
@Composable
fun PerfilUI() {
    val email = Firebase.auth.currentUser?.email ?: ""
    val displayName = Firebase.auth.currentUser?.displayName ?: "Usuario"
    val photoUrl =
        Firebase.auth.currentUser
            ?.photoUrl
            ?.toString()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F2)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            Text(
                text = "Mi perfil",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 32.dp),
            )

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(80.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = CircleShape,
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (photoUrl != null) {
                            AsyncImage(
                                model = photoUrl,
                                contentDescription = "Avatar",
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ProfileMenuItem(
                        icon = Icons.Default.Phone,
                        title = "Ayuda y soporte",
                        onClick = {
                            val intent =
                                Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:")
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf("ucb.app.dev@gmail.com"))
                                    putExtra(Intent.EXTRA_SUBJECT, "Soporte - Aplicación UCB Docentes")
                                }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast
                                    .makeText(
                                        context,
                                        "No se pudo abrir la aplicación de correo",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                            }
                        },
                    )

                    Divider(
                        color = Color.Gray.copy(alpha = 0.2f),
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                    ProfileMenuItem(
                        icon = Icons.Default.Info,
                        title = "Acerca de",
                        onClick = { showAboutDialog = true },
                    )

                    Divider(
                        color = Color.Gray.copy(alpha = 0.2f),
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                    ProfileMenuItem(
                        icon = Icons.Default.ExitToApp,
                        title = "Cerrar sesión",
                        onClick = { showLogoutDialog = true },
                        textColor = Color.Red,
                        iconColor = Color.Red,
                    )
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(text = "Cerrar sesión")
            },
            text = {
                Text(text = "¿Estás seguro de que deseas cerrar sesión?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        Firebase.auth.signOut()
                        showLogoutDialog = false

                        Toast
                            .makeText(
                                context,
                                "Sesión cerrada exitosamente",
                                Toast.LENGTH_SHORT,
                            ).show()

                        activity?.finishAffinity()
                    },
                ) {
                    Text(
                        text = "Cerrar sesión",
                        color = Color.Red,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                ) {
                    Text("Cancelar")
                }
            },
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Text(
                    text = "UCB Docentes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Column {
                    Text(
                        text = "Esta aplicación está destinada a los docentes de la Universidad Católica Boliviana para el registro y seguimiento del progreso académico de sus estudiantes.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    Text(
                        text = "Actualmente se encuentra en etapa de desarrollo y mejora continua. Tus observaciones y comentarios nos ayudan a crear una mejor experiencia educativa.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = "Versión",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = "Versión 1.0.0 (Beta)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showAboutDialog = false },
                ) {
                    Text("Entendido")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAboutDialog = false
                        val intent =
                            Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf("ucb.app.dev@gmail.com"))
                                putExtra(Intent.EXTRA_SUBJECT, "Feedback - Aplicación UCB Docentes")
                                putExtra(Intent.EXTRA_TEXT, "Hola, me gustaría compartir mi feedback sobre la aplicación:\n\n")
                            }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast
                                .makeText(
                                    context,
                                    "No se pudo abrir la aplicación de correo",
                                    Toast.LENGTH_SHORT,
                                ).show()
                        }
                    },
                ) {
                    Text("Enviar feedback")
                }
            },
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                modifier = Modifier.weight(1f),
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Ir",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
