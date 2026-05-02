package com.example.boardingbookingapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.ui.components.ModernBackground
import com.example.boardingbookingapp.ui.components.ModernButton
import com.example.boardingbookingapp.ui.components.ModernCard
import com.example.boardingbookingapp.ui.components.ModernTextField
import com.example.boardingbookingapp.ui.theme.ModernPrimary
import com.example.boardingbookingapp.ui.theme.ModernTextPrimary
import com.example.boardingbookingapp.ui.theme.ModernTextSecondary
import com.example.boardingbookingapp.ui.theme.ModernTextTertiary

@Composable
fun RegisterScreen(
    onRegistrationComplete: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val email by viewModel.pendingEmail.collectAsState()
    var displayName by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) onRegistrationComplete()
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
            Spacer(Modifier.height(48.dp))
            Text("Complete Profile", color = ModernTextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Set up your student identity", color = ModernTextSecondary, fontSize = 15.sp)

            Spacer(Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(ModernPrimary.copy(alpha = 0.05f))
                    .border(2.dp, ModernPrimary.copy(alpha = 0.2f), CircleShape)
                    .clickable { /* open photo picker */ },
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = ModernPrimary, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Add Photo", color = ModernPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(40.dp))

            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Your Student Email", color = ModernTextSecondary, fontSize = 13.sp)
                    Text(email, color = ModernTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(32.dp))

                    ModernTextField(
                        value         = displayName,
                        onValueChange = { displayName = it },
                        label         = "FULL NAME",
                        placeholder   = "How should we call you?",
                        isError       = state is AuthState.Error,
                        errorMessage  = if (state is AuthState.Error) (state as AuthState.Error).message else ""
                    )

                    Spacer(Modifier.height(40.dp))

                    ModernButton(
                        text      = "Complete Registration",
                        onClick   = { keyboard?.hide(); onRegistrationComplete() },
                        isLoading = state is AuthState.Loading,
                        enabled   = displayName.isNotBlank(),
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
