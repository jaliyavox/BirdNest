package com.example.boardingbookingapp.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.ui.components.GlassButton
import com.example.boardingbookingapp.ui.components.GlassCard
import com.example.boardingbookingapp.ui.components.GlassTextField
import com.example.boardingbookingapp.ui.components.GradientBackground
import com.example.boardingbookingapp.ui.theme.ErrorRed
import com.example.boardingbookingapp.ui.theme.TextSecondary

@Composable
fun OwnerRegisterScreen(
    onRegistered: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(state) {
        if (state is AuthState.NeedsKyc) onRegistered()
    }

    fun submit() {
        if (password != confirmPassword) {
            localError = "Passwords do not match"
            return
        }
        if (password.length < 6) {
            localError = "Password must be at least 6 characters"
            return
        }
        localError = ""
        keyboard?.hide()
        viewModel.registerOwner(name.trim(), email.trim(), password, phone.trim())
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
                Text("Create Owner Account", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(32.dp))
            Text("Register as Property Owner", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("You'll need to complete KYC verification", color = TextSecondary, fontSize = 13.sp)

            Spacer(Modifier.height(32.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    GlassTextField(
                        value         = name,
                        onValueChange = { name = it },
                        label         = "FULL NAME",
                        hint          = "Your legal name",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )
                    Spacer(Modifier.height(16.dp))

                    GlassTextField(
                        value         = email,
                        onValueChange = { email = it },
                        label         = "EMAIL",
                        hint          = "owner@example.com",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    )
                    Spacer(Modifier.height(16.dp))

                    GlassTextField(
                        value         = phone,
                        onValueChange = { phone = it },
                        label         = "PHONE NUMBER",
                        hint          = "07X XXXXXXX",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    )
                    Spacer(Modifier.height(16.dp))

                    GlassTextField(
                        value         = password,
                        onValueChange = { password = it; localError = "" },
                        label         = "PASSWORD",
                        hint          = "Min 6 characters",
                        isError       = localError.isNotEmpty() || state is AuthState.Error,
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
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
                    Spacer(Modifier.height(16.dp))

                    GlassTextField(
                        value         = confirmPassword,
                        onValueChange = { confirmPassword = it; localError = "" },
                        label         = "CONFIRM PASSWORD",
                        hint          = "Repeat password",
                        isError       = localError.isNotEmpty(),
                        errorMessage  = localError.ifEmpty {
                            if (state is AuthState.Error) (state as AuthState.Error).message else ""
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    )

                    Spacer(Modifier.height(28.dp))

                    GlassButton(
                        text      = "Continue to KYC",
                        onClick   = { submit() },
                        isLoading = state is AuthState.Loading,
                        enabled   = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank(),
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
