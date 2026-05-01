package com.example.birdnest.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdnest.data.model.PaymentStatus
import com.example.birdnest.ui.components.*
import com.example.birdnest.ui.theme.*

private data class PaymentRecord(
    val month: String,
    val amount: Int,
    val status: PaymentStatus,
    val dueDate: String,
    val paidDate: String?,
)

private val MOCK_PAYMENTS = listOf(
    PaymentRecord("May 2026",   18000, PaymentStatus.PENDING, "May 5", null),
    PaymentRecord("April 2026", 18000, PaymentStatus.PAID,    "Apr 5", "Apr 3"),
    PaymentRecord("March 2026", 18000, PaymentStatus.PAID,    "Mar 5", "Mar 4"),
    PaymentRecord("Feb 2026",   18000, PaymentStatus.PAID,    "Feb 5", "Feb 6"),
    PaymentRecord("Jan 2026",   18000, PaymentStatus.OVERDUE, "Jan 5", null),
    PaymentRecord("Dec 2025",   18000, PaymentStatus.PAID,    "Dec 5", "Dec 4"),
)

@Composable
fun RentTrackerScreen(
    onBack: () -> Unit,
    onUploadReceipt: () -> Unit = {},
) {
    val currentMonth = MOCK_PAYMENTS.first()
    val totalPaid = MOCK_PAYMENTS.count { it.status == PaymentStatus.PAID }

    ModernBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ModernTextPrimary)
                    }
                    Text("Rent Tracker", color = ModernTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                }
            }

            item {
                // Current month overview card
                ModernCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Current Month", color = ModernTextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            ModernStatusBadge(currentMonth.status)
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Rs. ${"%,d".format(currentMonth.amount)}", color = ModernTextPrimary, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(8.dp))
                        Text("Due on ${currentMonth.dueDate} · SLIIT Residence", color = ModernTextSecondary, fontSize = 14.sp)
                        
                        Spacer(Modifier.height(24.dp))

                        if (currentMonth.status != PaymentStatus.PAID) {
                            ModernButton(
                                text = "Upload Receipt",
                                onClick = onUploadReceipt,
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(SuccessGreen.copy(alpha = 0.1f))
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.CheckCircle, null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                                    Text("Payment Confirmed", color = SuccessGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SummaryStatCard("Total Paid", "Rs. ${"%,d".format(MOCK_PAYMENTS.sumOf { if (it.status == PaymentStatus.PAID) it.amount else 0 })}", modifier = Modifier.weight(1f))
                    SummaryStatCard("On Time", "${totalPaid}/${MOCK_PAYMENTS.size}", modifier = Modifier.weight(1f))
                }
            }

            item {
                Text("Payment History", color = ModernTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            items(MOCK_PAYMENTS) { payment ->
                PaymentHistoryCard(payment)
            }
        }
    }
}

@Composable
private fun SummaryStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    ModernCard(modifier = modifier) {
        Column {
            Text(label, color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(value, color = ModernPrimary, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun PaymentHistoryCard(payment: PaymentRecord) {
    ModernCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ModernBlueSoft),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = when (payment.status) {
                        PaymentStatus.PAID    -> Icons.Default.CheckCircle
                        PaymentStatus.PENDING -> Icons.Default.Schedule
                        PaymentStatus.OVERDUE -> Icons.Default.Warning
                    },
                    contentDescription = null,
                    tint = when (payment.status) {
                        PaymentStatus.PAID    -> SuccessGreen
                        PaymentStatus.PENDING -> WarningAmber
                        PaymentStatus.OVERDUE -> ErrorRed
                    },
                    modifier = Modifier.size(20.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(payment.month, color = ModernTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = if (payment.paidDate != null) "Paid on ${payment.paidDate}" else "Due ${payment.dueDate}",
                    color = ModernTextSecondary,
                    fontSize = 12.sp,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Rs. ${"%,d".format(payment.amount)}", color = ModernTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                ModernStatusBadge(payment.status)
            }
        }
    }
}

@Composable
private fun ModernStatusBadge(status: PaymentStatus) {
    val (text, color) = when (status) {
        PaymentStatus.PAID    -> "PAID"    to SuccessGreen
        PaymentStatus.PENDING -> "PENDING" to WarningAmber
        PaymentStatus.OVERDUE -> "OVERDUE" to ErrorRed
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(text, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
