package com.example.boardingbookingapp.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import com.example.boardingbookingapp.ui.components.ModernBackground
import com.example.boardingbookingapp.ui.components.ModernButton
import com.example.boardingbookingapp.ui.components.ModernCard
import com.example.boardingbookingapp.ui.components.ModernTextField
import com.example.boardingbookingapp.ui.theme.ModernPrimary
import com.example.boardingbookingapp.ui.theme.ModernTextPrimary
import com.example.boardingbookingapp.ui.theme.ModernTextSecondary
import com.example.boardingbookingapp.ui.theme.ModernTextTertiary

@Composable
fun LoginScreen(
    onOtpSent: (String) -> Unit,
    onNavigateToOwnerLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(state) {
        if (state is AuthState.OtpSent) {
            onOtpSent(email)
            viewModel.clearError()
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
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = ModernPrimary,
                    modifier = Modifier.size(40.dp),
                )
            }
            Spacer(Modifier.height(24.dp))
            Text("Welcome Back", color = ModernTextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("Student Housing & Roommate Finder", color = ModernTextSecondary, fontSize = 14.sp, textAlign = TextAlign.Center)

            Spacer(Modifier.height(48.dp))

            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Student Sign In", color = ModernTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Use your SLIIT student email to continue", color = ModernTextSecondary, fontSize = 14.sp)
                    Spacer(Modifier.height(32.dp))

                    ModernTextField(
                        value         = email,
                        onValueChange = {
                            email = it
                            if (state is AuthState.InvalidDomain || state is AuthState.Error) viewModel.clearError()
                        },
                        label = "EMAIL ADDRESS",
                        placeholder  = "yourname@my.sliit.lk",
                        isError      = state is AuthState.InvalidDomain || state is AuthState.Error,
                        errorMessage = when (state) {
                            is AuthState.InvalidDomain -> "Only @my.sliit.lk emails are allowed"
                            is AuthState.Error         -> (state as AuthState.Error).message
                            else                       -> ""
                        }
                    )
                    Spacer(Modifier.height(32.dp))

                    ModernButton(
                        text      = "Continue",
                        onClick   = { keyboard?.hide(); viewModel.sendOtp(email.trim()) },
                        isLoading = state is AuthState.Loading,
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Are you an owner? ", color = ModernTextSecondary, fontSize = 14.sp)
                Text(
                    text       = "Owner Login",
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
