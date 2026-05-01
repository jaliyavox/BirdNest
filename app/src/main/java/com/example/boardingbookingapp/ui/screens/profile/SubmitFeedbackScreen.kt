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
import androidx.compose.material.icons.filled.ThumbUp
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
import com.example.boardingbookingapp.data.model.FeedbackCategory
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val CATEGORY_LABELS = mapOf(
    FeedbackCategory.GENERAL             to "General",
    FeedbackCategory.BUG_REPORT          to "Bug Report",
    FeedbackCategory.FEATURE_REQUEST     to "Feature Request",
    FeedbackCategory.LISTING_COMPLAINT   to "Listing Issue",
    FeedbackCategory.USER_COMPLAINT      to "User Conduct",
)

@Composable
fun SubmitFeedbackScreen(
    onBack: () -> Unit,
) {
    var selectedCategory by remember { mutableStateOf(FeedbackCategory.GENERAL) }
    var message by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }
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
                Text("Submit Feedback", color = ModernTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))

            if (submitted) {
                FeedbackSuccessState(onBack = onBack)
            } else {
                FeedbackForm(
                    selectedCategory = selectedCategory,
                    message = message,
                    isLoading = isLoading,
                    onCategorySelected = { selectedCategory = it },
                    onMessageChange = { message = it },
                    onSubmit = {
                        scope.launch {
                            isLoading = true
                            delay(1200)
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
private fun FeedbackForm(
    selectedCategory: FeedbackCategory,
    message: String,
    isLoading: Boolean,
    onCategorySelected: (FeedbackCategory) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Text("Category", color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(10.dp))

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val entries = FeedbackCategory.entries
        entries.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { cat ->
                    ModernFilterChip(
                        label = CATEGORY_LABELS[cat] ?: cat.name,
                        selected = selectedCategory == cat,
                        onClick = { onCategorySelected(cat) },
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(24.dp))

    Text("Message", color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(10.dp))

    FeedbackTextArea(
        value = message,
        onValueChange = onMessageChange,
        placeholder = "Describe your feedback in detail…",
    )

    Spacer(Modifier.height(32.dp))

    ModernButton(
        text = "Submit Feedback",
        onClick = onSubmit,
        isLoading = isLoading,
        enabled = message.length >= 10,
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = "Minimum 10 characters required",
        color = ModernTextTertiary,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(Modifier.height(32.dp))
}

@Composable
private fun FeedbackTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        if (value.isEmpty()) {
            Text(placeholder, color = ModernTextTertiary, fontSize = 15.sp)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
            textStyle = LocalTextStyle.current.copy(color = ModernTextPrimary, fontSize = 15.sp),
            cursorBrush = SolidColor(ModernPrimary),
        )
    }
}

@Composable
private fun FeedbackSuccessState(onBack: () -> Unit) {
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
            Icon(Icons.Default.ThumbUp, null, tint = ModernPrimary, modifier = Modifier.size(48.dp))
        }
        Text("Thank You!", color = ModernTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Your feedback has been submitted.\nOur team will review it shortly.",
            color = ModernTextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
        )
        Spacer(Modifier.height(8.dp))
        ModernButton(text = "Back to Profile", onClick = onBack)
    }
}
