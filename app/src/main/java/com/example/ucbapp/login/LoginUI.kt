package com.example.ucbapp.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucbapp.R
import com.example.ucbapp.service.InternetConnection.Companion.isConnected

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginUI(modifier: Modifier = Modifier) {
    var userSignIn by remember { mutableStateOf("") }
    var passwordSignIn by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var context = LocalContext.current
    val viewModel: LoginViewModel = viewModel()

    val loginState by viewModel.loginState.collectAsState(LoginViewModel.LoginState.Init)

    when (loginState) {
        is LoginViewModel.LoginState.Init -> {
            Toast.makeText(context, "Init", Toast.LENGTH_LONG).show()
        }
        is LoginViewModel.LoginState.Error -> {
            Toast.makeText(context, (loginState as LoginViewModel.LoginState.Error).message, Toast.LENGTH_LONG).show()
        }
        is LoginViewModel.LoginState.Successful -> {
            Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show()
        }
        is LoginViewModel.LoginState.Loading -> {
            Toast.makeText(context, "Loading....", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.signin_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 48.dp),
        )

        Text(
            text = "Bienvenido de nuevo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 24.dp),
        )

        Text(
            text = "Correo electrónico",
            style = MaterialTheme.typography.bodyMedium,
            modifier =
                Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
        )

        OutlinedTextField(
            value = userSignIn,
            onValueChange = { userSignIn = it },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            colors =
                OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                ),
        )
        Text(
            text = "Contraseña",
            style = MaterialTheme.typography.bodyMedium,
            modifier =
                Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
        )

        OutlinedTextField(
            value = passwordSignIn,
            onValueChange = { passwordSignIn = it },
            modifier =
                Modifier
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
            colors =
                OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                ),
        )

        Button(
            onClick = {
                if (!isConnected(context)) {
                    Toast.makeText(context, "No tiene acceso a internet", Toast.LENGTH_LONG).show()
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            shape = RoundedCornerShape(24.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004787),
                ),
        ) {
            Text(
                text = stringResource(R.string.signin_button),
                color = Color.White,
            )
        }

        Text(
            text = "Or sign in with",
            modifier = Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
        )

        OutlinedButton(
            onClick = { viewModel.doLogin(userSignIn, passwordSignIn) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.Gray),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black,
                ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Google icon",
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Continue with Google")
            }
        }
    }
}
