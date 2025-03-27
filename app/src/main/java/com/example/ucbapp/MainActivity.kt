package com.example.ucbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.UcbappTheme
import com.example.ucbapp.login.LoginUI
import dagger.hilt.android.AndroidEntryPoint
import io.sentry.Sentry

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UcbappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginUI(modifier = Modifier.padding(innerPadding))
                }
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



