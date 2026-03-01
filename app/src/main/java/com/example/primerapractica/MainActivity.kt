package com.example.primerapractica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.primerapractica.presentation.navigation.AppNavigation
import com.example.primerapractica.ui.theme.PrimeraPracticaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrimeraPracticaTheme {
                AppNavigation()
            }
        }
    }
}