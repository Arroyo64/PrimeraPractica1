package com.example.primerapractica.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.primerapractica.presentation.navigation.NavScreen
import com.example.primerapractica.presentation.viewmodel.AuthEvent
import com.example.primerapractica.presentation.viewmodel.AuthState
import com.example.primerapractica.presentation.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    authVm: AuthViewModel = koinViewModel()
) {
    val state by authVm.state.collectAsState()
    val loading = state is AuthState.Loading
    val error = (state as? AuthState.Error)?.message

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Eventos de navegación
    LaunchedEffect(Unit) {
        authVm.events.collect { ev ->
            when (ev) {
                AuthEvent.NavigateHome -> {
                    navController.navigate(NavScreen.Home.route) {
                        popUpTo(NavScreen.Register.route) { inclusive = true }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña (min 6)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (error != null) {
            Spacer(Modifier.height(10.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(20.dp))

        Button(
            enabled = !loading && email.isNotBlank() && password.length >= 6,
            onClick = { authVm.register(email, password) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(if (loading) "Registrando..." else "Registrar")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            modifier = Modifier.clickable {
                authVm.clearError()
                navController.navigate(NavScreen.Login.route) {
                    popUpTo(NavScreen.Register.route) { inclusive = true }
                }
            }
        )
    }
}