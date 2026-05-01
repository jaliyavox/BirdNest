package com.example.boardingbookingapp.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boardingbookingapp.ui.components.ModernBackground
import com.example.boardingbookingapp.ui.components.ModernButton
import com.example.boardingbookingapp.ui.components.ModernCard
import com.example.boardingbookingapp.ui.theme.ModernBlueSoft
import com.example.boardingbookingapp.ui.theme.ModernPrimary
import com.example.boardingbookingapp.ui.theme.ModernTextPrimary
import com.example.boardingbookingapp.ui.theme.ModernTextSecondary
import com.example.boardingbookingapp.ui.theme.ModernTextTertiary
import kotlinx.coroutines.delay

@Composable
fun OtpVerifyScreen(
    email: String,
    onVerified: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var otp by remember { mutableStateOf("") }
    var countdown by remember { mutableIntStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        canResend = true
    }

    LaunchedEffect(state) {
        if (state is AuthState.NeedsProfile) onVerified()
    }

    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ModernTextPrimary)
                }
            }

            Spacer(Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(ModernPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Email, contentDescription = null, tint = ModernPrimary, modifier = Modifier.size(40.dp))
            }

            Spacer(Modifier.height(24.dp))
            Text("Verification", color = ModernTextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Enter the 6-digit code we sent to\n$email",
                color = ModernTextSecondary,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(40.dp))

            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OtpInputRow(
                        value         = otp,
                        onValueChange = {
                            if (it.length <= 6 && it.all { c -> c.isDigit() }) otp = it
                        },
                        isError = state is AuthState.Error,
                    )

                    if (state is AuthState.Error) {
                        Spacer(Modifier.height(8.dp))
                        Text((state as AuthState.Error).message, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(32.dp))

                    ModernButton(
                        text      = "Verify Code",
                        onClick   = { viewModel.verifyOtp(otp) },
                        isLoading = state is AuthState.Loading,
                        enabled   = otp.length == 6,
                    )

                    Spacer(Modifier.height(24.dp))

                    if (canResend) {
                        Text(
                            text       = "Resend OTP",
                            color      = ModernPrimary,
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier   = Modifier.clickable {
                                canResend = false
                                countdown = 60
                                otp = ""
                                viewModel.clearError()
                            },
                        )
                    } else {
                        Text("Resend in ${countdown}s", color = ModernTextTertiary, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun OtpInputRow(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        cursorBrush = SolidColor(ModernPrimary),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(6) { index ->
                    val char = value.getOrNull(index)?.toString() ?: ""
                    val isCurrent = value.length == index
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(width = 46.dp, height = 56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (char.isNotEmpty() || isCurrent) ModernBlueSoft else Color(0xFFF0F2F5))
                            .then(
                                if (isCurrent) Modifier.border(1.5.dp, ModernPrimary, RoundedCornerShape(12.dp))
                                else if (isError) Modifier.border(1.5.dp, Color.Red, RoundedCornerShape(12.dp))
                                else Modifier
                            ),
                    ) {
                        Text(
                            text       = char,
                            color      = ModernTextPrimary,
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign  = TextAlign.Center,
                        )
                    }
                }
            }
        },
    )
}
