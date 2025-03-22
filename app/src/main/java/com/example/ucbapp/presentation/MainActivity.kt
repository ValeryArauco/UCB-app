package com.example.ucbapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import com.example.compose.UcbappTheme
import com.example.ucbapp.presentation.components.UserMenu

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UcbappTheme {
                Surface(color = colorScheme.background) {
                    UserMenu()
                }
            }
        }
    }
}