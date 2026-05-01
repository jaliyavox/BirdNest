package com.example.boardingbookingapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardingbookingapp.data.model.TicketPriority
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val PRIORITY_LABELS = mapOf(
    TicketPriority.LOW    to "Low",
    TicketPriority.MEDIUM to "Medium",
    TicketPriority.HIGH   to "High",
    TicketPriority.URGENT to "Urgent",
)

private val PRIORITY_COLORS = mapOf(
    TicketPriority.LOW    to SuccessGreen,
    TicketPriority.MEDIUM to WarningAmber,
    TicketPriority.HIGH   to ErrorRed,
    TicketPriority.URGENT to ErrorRed,
)

@Composable
fun SubmitTicketScreen(
    onBack: () -> Unit,
) {
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TicketPriority.MEDIUM) }
    var submitted by remember { mutableStateOf(false) }
    var ticketRef by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ModernTextPrimary)
                }
                Text("Support Ticket", color = ModernTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))

            if (submitted) {
                TicketSuccessState(ticketRef = ticketRef, onBack = onBack)
            } else {
                TicketForm(
                    subject = subject,
                    description = description,
                    selectedPriority = selectedPriority,
                    isLoading = isLoading,
                    onSubjectChange = { subject = it },
                    onDescriptionChange = { description = it },
                    onPrioritySelected = { selectedPriority = it },
                    onSubmit = {
                        scope.launch {
                            isLoading = true
                            delay(1200)
                            ticketRef = "TKT-${(100000..999999).random()}"
                            isLoading = false
                            submitted = true
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun TicketForm(
    subject: String,
    description: String,
    selectedPriority: TicketPriority,
    isLoading: Boolean,
    onSubjectChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPrioritySelected: (TicketPriority) -> Unit,
    onSubmit: () -> Unit,
) {
    ModernTextField(
        value = subject,
        onValueChange = onSubjectChange,
        label = "SUBJECT",
        placeholder = "Brief description of the issue",
    )

    Spacer(Modifier.height(24.dp))

    Text("Priority", color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(10.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        TicketPriority.entries.forEach { priority ->
            ModernFilterChip(
                label = PRIORITY_LABELS[priority] ?: priority.name,
                selected = selectedPriority == priority,
                onClick = { onPrioritySelected(priority) },
            )
        }
    }

    Spacer(Modifier.height(24.dp))

    Text("Description", color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(10.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        if (description.isEmpty()) {
            Text("Describe the issue in detail…", color = ModernTextTertiary, fontSize = 15.sp)
        }
        BasicTextField(
            value = description,
            onValueChange = onDescriptionChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
            textStyle = LocalTextStyle.current.copy(color = ModernTextPrimary, fontSize = 15.sp),
            cursorBrush = SolidColor(ModernPrimary),
        )
    }

    Spacer(Modifier.height(32.dp))

    ModernButton(
        text = "Submit Ticket",
        onClick = onSubmit,
        isLoading = isLoading,
        enabled = subject.isNotBlank() && description.length >= 20,
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = "Subject required · Minimum 20 characters in description",
        color = ModernTextTertiary,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(Modifier.height(32.dp))
}

@Composable
private fun TicketSuccessState(ticketRef: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(ModernBlueSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.ConfirmationNumber, null, tint = ModernPrimary, modifier = Modifier.size(48.dp))
        }
        Text("Ticket Submitted!", color = ModernTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(ModernBlueSoft)
                .padding(horizontal = 20.dp, vertical = 10.dp),
        ) {
            Text(ticketRef, color = ModernPrimary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        }

        Text(
            text = "Save your reference number above.\nOur support team will respond within 48 hours.",
            color = ModernTextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
        )
        Spacer(Modifier.height(8.dp))
        ModernButton(text = "Back to Profile", onClick = onBack)
    }
}
