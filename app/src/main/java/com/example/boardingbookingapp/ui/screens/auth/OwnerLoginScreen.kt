package com.example.boardingbookingapp.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.ui.components.GlassButton
import com.example.boardingbookingapp.ui.components.GlassCard
import com.example.boardingbookingapp.ui.components.GlassOutlineButton
import com.example.boardingbookingapp.ui.components.GlassTextField
import com.example.boardingbookingapp.ui.components.GradientBackground
import com.example.boardingbookingapp.ui.theme.CyanAccent
import com.example.boardingbookingapp.ui.theme.ErrorRed
import com.example.boardingbookingapp.ui.theme.TextSecondary
import com.example.boardingbookingapp.ui.theme.TextTertiary
import com.example.boardingbookingapp.ui.theme.VioletLight

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

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }

            Spacer(Modifier.height(24.dp))
            Icon(
                imageVector  = Icons.Default.BusinessCenter,
                contentDescription = null,
                tint         = CyanAccent,
                modifier     = Modifier.size(48.dp),
            )
            Spacer(Modifier.height(12.dp))
            Text("Owner Login", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp)
            Text("Manage your properties", color = TextSecondary, fontSize = 14.sp, textAlign = TextAlign.Center)

            Spacer(Modifier.height(40.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    GlassTextField(
                        value         = email,
                        onValueChange = { email = it; viewModel.clearError() },
                        label         = "EMAIL ADDRESS",
                        hint          = "owner@example.com",
                        isError       = state is AuthState.Error,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    )
                    Spacer(Modifier.height(16.dp))

                    GlassTextField(
                        value         = password,
                        onValueChange = { password = it; viewModel.clearError() },
                        label         = "PASSWORD",
                        hint          = "Enter your password",
                        isError       = state is AuthState.Error,
                        errorMessage  = if (state is AuthState.Error) (state as AuthState.Error).message else "",
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboard?.hide()
                            viewModel.loginOwner(email.trim(), password)
                        }),
                        trailingIcon  = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                )
                            }
                        },
                    )
                    Spacer(Modifier.height(24.dp))

                    GlassButton(
                        text      = "Login",
                        onClick   = { keyboard?.hide(); viewModel.loginOwner(email.trim(), password) },
                        isLoading = state is AuthState.Loading,
                        enabled   = email.isNotBlank() && password.isNotBlank(),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            GlassOutlineButton(
                text    = "Register as Owner",
                onClick = onNavigateToRegister,
            )

            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Are you a student? ", color = TextTertiary, fontSize = 13.sp)
                Text(
                    text       = "Student Login",
                    color      = VioletLight,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.clickable { onBack() },
                )
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
