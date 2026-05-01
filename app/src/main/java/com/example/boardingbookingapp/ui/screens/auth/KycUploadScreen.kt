package com.example.birdnest.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.birdnest.ui.components.*
import com.example.birdnest.ui.theme.*

@Composable
fun KycUploadScreen(
    onSubmitted: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var nicUploaded by remember { mutableStateOf(false) }
    var selfieUploaded by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is AuthState.KycPending) onSubmitted()
    }

    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ModernTextPrimary)
                }
                Text("Verification", color = ModernTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))
            Text("Verify Identity", color = ModernTextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Upload your identification to start listing properties. Secure and encrypted.",
                color = ModernTextSecondary,
                fontSize = 15.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            ModernCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    KycSlot(
                        icon      = Icons.Default.Badge,
                        title     = "National ID / NIC",
                        subtitle  = "Clear photo of front & back",
                        uploaded  = nicUploaded,
                        onClick   = { nicUploaded = true },
                    )
                    KycSlot(
                        icon      = Icons.Default.Person,
                        title     = "Selfie Verification",
                        subtitle  = "Photo showing your face clearly",
                        uploaded  = selfieUploaded,
                        onClick   = { selfieUploaded = true },
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            ModernButton(
                text      = "Submit Documents",
                onClick   = { viewModel.submitKyc() },
                isLoading = state is AuthState.Loading,
                enabled   = nicUploaded && selfieUploaded,
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun KycSlot(
    icon: ImageVector,
    title: String,
    subtitle: String,
    uploaded: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (uploaded) SuccessGreen.copy(alpha = 0.05f) else ModernBackground)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (uploaded) SuccessGreen.copy(alpha = 0.1f) else ModernBlueSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (uploaded) Icons.Default.Check else icon,
                contentDescription = null,
                tint = if (uploaded) SuccessGreen else ModernPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = ModernTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(
                text = if (uploaded) "Document attached" else subtitle,
                color = if (uploaded) SuccessGreen else ModernTextSecondary,
                fontSize = 12.sp,
                fontWeight = if (uploaded) FontWeight.Bold else FontWeight.Normal
            )
        }
        if (!uploaded) {
            Icon(Icons.Default.AddAPhoto, null, tint = ModernTextTertiary, modifier = Modifier.size(20.dp))
        }
    }
}
