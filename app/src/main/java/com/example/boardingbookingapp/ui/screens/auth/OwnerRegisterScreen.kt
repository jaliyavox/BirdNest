package com.example.boardingbookingapp.ui.screens.auth

import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                Text("Create Owner Account", color = ModernTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                "You'll need to complete KYC verification after registration",
                color    = ModernTextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
            )

            Spacer(Modifier.height(28.dp))

            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ModernTextField(
                        value           = name,
                        onValueChange   = { name = it },
                        label           = "FULL NAME",
                        placeholder     = "Your legal name",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )

                    ModernTextField(
                        value           = email,
                        onValueChange   = { email = it },
                        label           = "EMAIL",
                        placeholder     = "owner@example.com",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    )

                    ModernTextField(
                        value           = phone,
                        onValueChange   = { phone = it },
                        label           = "PHONE NUMBER",
                        placeholder     = "07X XXXXXXX",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    )

                    ModernTextField(
                        value                = password,
                        onValueChange        = { password = it; localError = "" },
                        label                = "PASSWORD",
                        placeholder          = "Min 6 characters",
                        isError              = localError.isNotEmpty() || state is AuthState.Error,
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
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

                    ModernTextField(
                        value                = confirmPassword,
                        onValueChange        = { confirmPassword = it; localError = "" },
                        label                = "CONFIRM PASSWORD",
                        placeholder          = "Repeat password",
                        isError              = localError.isNotEmpty(),
                        errorMessage         = localError.ifEmpty {
                            if (state is AuthState.Error) (state as AuthState.Error).message else ""
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    )

                    ModernButton(
                        text      = "Continue to KYC",
                        onClick   = { submit() },
                        isLoading = state is AuthState.Loading,
                        enabled   = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() &&
                            password.isNotBlank() && confirmPassword.isNotBlank(),
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
