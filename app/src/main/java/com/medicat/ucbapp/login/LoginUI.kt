package com.medicat.ucbapp.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicat.ucbapp.R

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginUI(onSuccess: () -> Unit) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel: LoginViewModel = hiltViewModel()

    val loginState by viewModel.loginState.collectAsState(LoginViewModel.LoginState.Init)

    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { /* No necesitamos hacer nada con el resultado */ },
        )

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginViewModel.LoginState.Error -> {
                errorMessage = (loginState as LoginViewModel.LoginState.Error).message
                isLoading = false
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
            is LoginViewModel.LoginState.Successful -> {
                isLoading = false
                Toast.makeText(context, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show()
                onSuccess()
            }
            is LoginViewModel.LoginState.Loading -> {
                isLoading = true
                errorMessage = null
            }
            else -> {
                isLoading = false
                errorMessage = null
            }
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "UCB APP",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 14.dp),
            )

            Text(
                text = "Bienvenido de nuevo",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            Image(
                painter = painterResource(id = R.drawable.ucb_logo),
                contentDescription = "UCB Logo",
                modifier =
                    Modifier
                        .size(450.dp)
                        .padding(bottom = 32.dp),
            )

            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                )
                Text(
                    text = "Verificando credenciales...",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                OutlinedButton(
                    onClick = {
                        viewModel.signInWithGoogle(context, launcher)
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.DarkGray,
                        ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = "Google icon",
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Continuar con Google")
                    }
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Sólo usuarios con correo institucional (@ucb.edu.bo) pueden acceder",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}
