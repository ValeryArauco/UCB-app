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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicat.ucbapp.R

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginUI(onSuccess: () -> Unit) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel: LoginViewModel = hiltViewModel()

    val loginState by viewModel.loginState.collectAsState(LoginViewModel.LoginState.Init)

    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { },
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

    val gradientBrush =
        Brush.linearGradient(
            colors =
                listOf(
                    Color(0xFF004C8D),
                    MaterialTheme.colorScheme.primary,
                ),
            start = Offset(0f, 0f),
            end = Offset(1000f, 1000f),
        )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(horizontal = 32.dp, vertical = 50.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = Color(0xFFF8F9FA),
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, vertical = 32.dp)
                        .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                LogoSection()

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    LoadingSection()
                } else {
                    LoginForm(
                        email = email,
                        onEmailChange = { email = it },
                        password = password,
                        onPasswordChange = { password = it },
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
                        onLogin = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                viewModel.signInWithEmailPassword(email, password)
                            } else {
                                errorMessage = "Por favor complete todos los campos"
                            }
                        },
                        onGoogleLogin = { viewModel.signInWithGoogle(context, launcher) },
                    )
                }

                errorMessage?.let { message ->
                    ErrorMessage(message = message)
                }

                Spacer(modifier = Modifier.height(8.dp))

                FooterText()
            }
        }
    }
}

@Composable
private fun LogoSection() {
    Spacer(modifier = Modifier.height(16.dp))
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(80.dp)
                    .background(
                        brush =
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF1E3A8A), MaterialTheme.colorScheme.primary),
                            ),
                        shape = RoundedCornerShape(20.dp),
                    ).padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.width(90.dp),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Medicat",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center,
            )
        }

        Text(
            text = "Registro de avance docente",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun LoadingSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp,
            modifier = Modifier.size(40.dp),
        )
        Text(
            text = "Verificando credenciales...",
            color = Color(0xFF64748B),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun LoginForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    onLogin: () -> Unit,
    onGoogleLogin: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ModernTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo institucional",
            placeholder = "docente@ucb.edu.bo",
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
        )

        ModernTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            placeholder = "••••••••",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            passwordVisible = passwordVisible,
            onPasswordVisibilityChange = onPasswordVisibilityChange,
        )

        Button(
            onClick = onLogin,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                ),
            contentPadding =
                PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp,
                ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Iniciar Sesión")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE5E7EB),
            )
            Text(
                text = "o",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFF9CA3AF),
                style = MaterialTheme.typography.bodySmall,
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE5E7EB),
            )
        }

        Button(
            onClick = onGoogleLogin,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                ),
            contentPadding =
                PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp,
                ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
}

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = Color(0xFF374151),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF9CA3AF),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
            )
        },
        trailingIcon =
            if (isPassword) {
                {
                    IconButton(onClick = { onPasswordVisibilityChange?.invoke() }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = Color(0xFF9CA3AF),
                        )
                    }
                }
            } else {
                null
            },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = Color(0xFF374151),
                unfocusedTextColor = Color(0xFF374151),
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFFAFAFA),
            ),
    )
}

@Composable
private fun ErrorMessage(message: String) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFFFEF2F2),
            ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFFECACA)),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFDC2626),
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = Color(0xFFDC2626),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun FooterText() {
    Text(
        text = "Solo para uso de docentes autorizados",
        color = Color(0xFF9CA3AF),
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        lineHeight = 18.sp,
    )
}
