package com.example.boardingbookingapp.ui.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.ui.components.ModernBackground
import com.example.boardingbookingapp.ui.components.ModernButton
import com.example.boardingbookingapp.ui.components.ModernCard
import com.example.boardingbookingapp.ui.components.ModernTextField
import com.example.boardingbookingapp.ui.theme.*

@Composable
fun OwnerLoginScreen(
    onLoggedIn: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) onLoggedIn()
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
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ModernTextPrimary)
                }
                Spacer(Modifier.width(8.dp))
                Text("Owner Login", color = ModernTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                "Manage your properties",
                color    = ModernTextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
            )

            Spacer(Modifier.height(32.dp))

            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ModernTextField(
                        value           = email,
                        onValueChange   = { email = it; viewModel.clearError() },
                        label           = "EMAIL ADDRESS",
                        placeholder     = "owner@example.com",
                        isError         = state is AuthState.Error,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    )

                    ModernTextField(
                        value                = password,
                        onValueChange        = { password = it; viewModel.clearError() },
                        label                = "PASSWORD",
                        placeholder          = "Enter your password",
                        isError              = state is AuthState.Error,
                        errorMessage         = if (state is AuthState.Error) (state as AuthState.Error).message else "",
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions      = KeyboardActions(onDone = {
                            keyboard?.hide()
                            viewModel.loginOwner(email.trim(), password)
                        }),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = ModernTextSecondary,
                                )
                            }
                        },
                    )

                    ModernButton(
                        text      = "Login",
                        onClick   = { keyboard?.hide(); viewModel.loginOwner(email.trim(), password) },
                        isLoading = state is AuthState.Loading,
                        enabled   = email.isNotBlank() && password.isNotBlank(),
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            ModernButton(
                text           = "Register as Owner",
                onClick        = onNavigateToRegister,
                containerColor = Color.White,
                contentColor   = ModernPrimary,
                modifier       = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ModernPrimary.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            )

            Spacer(Modifier.height(20.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text("Are you a student? ", color = ModernTextSecondary, fontSize = 13.sp)
                Text(
                    text       = "Student Login",
                    color      = ModernPrimary,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.clickable { onBack() },
                )
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
