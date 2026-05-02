package com.example.boardingbookingapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.ui.components.ModernBackground
import com.example.boardingbookingapp.ui.components.ModernButton
import com.example.boardingbookingapp.ui.components.ModernCard
import com.example.boardingbookingapp.ui.components.ModernTextField
import com.example.boardingbookingapp.ui.theme.*

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onNavigateToOwnerLogin: () -> Unit,
    onNavigateToRegister: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            viewModel.clearError()
            onLoggedIn()
        }
    }

    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(56.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(ModernPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Home, null, tint = ModernPrimary, modifier = Modifier.size(40.dp))
            }

            Spacer(Modifier.height(24.dp))
            Text("Welcome Back", color = ModernTextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("Student Housing & Roommate Finder", color = ModernTextSecondary, fontSize = 14.sp, textAlign = TextAlign.Center)

            Spacer(Modifier.height(48.dp))

            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Student Login", color = ModernTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Use your SLIIT student email", color = ModernTextSecondary, fontSize = 14.sp)
                    Spacer(Modifier.height(28.dp))

                    ModernTextField(
                        value         = email,
                        onValueChange = {
                            email = it
                            if (state is AuthState.Error || state is AuthState.InvalidDomain) viewModel.clearError()
                        },
                        label        = "STUDENT EMAIL",
                        placeholder  = "yourname@my.sliit.lk",
                        isError      = state is AuthState.InvalidDomain,
                        errorMessage = if (state is AuthState.InvalidDomain) "Only @my.sliit.lk emails are allowed" else "",
                    )

                    Spacer(Modifier.height(16.dp))

                    ModernTextField(
                        value                = password,
                        onValueChange        = { password = it },
                        label                = "PASSWORD",
                        placeholder          = "Your password",
                        visualTransformation = PasswordVisualTransformation(),
                        isError              = state is AuthState.Error,
                        errorMessage         = if (state is AuthState.Error) (state as AuthState.Error).message else "",
                    )

                    Spacer(Modifier.height(28.dp))

                    ModernButton(
                        text      = "Login",
                        onClick   = { keyboard?.hide(); viewModel.loginOwner(email.trim(), password) },
                        isLoading = state is AuthState.Loading,
                        enabled   = email.isNotBlank() && password.isNotBlank(),
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Register", color = ModernPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Are you an owner? ", color = ModernTextSecondary, fontSize = 14.sp)
                Text(
                    "Owner Login",
                    color      = ModernPrimary,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.clickable { onNavigateToOwnerLogin() },
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
