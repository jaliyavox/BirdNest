package com.example.boardingbookingapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardingbookingapp.ui.theme.CyanAccent
import com.example.boardingbookingapp.ui.theme.ErrorRed
import com.example.boardingbookingapp.ui.theme.GlassBorder
import com.example.boardingbookingapp.ui.theme.TextHint
import com.example.boardingbookingapp.ui.theme.TextPrimary
import com.example.boardingbookingapp.ui.theme.TextSecondary

private val FieldShape = RoundedCornerShape(14.dp)

@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hint: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 5,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue = when {
            isError   -> ErrorRed
            isFocused -> CyanAccent
            else      -> Color.White.copy(alpha = 0.30f)
        },
        label = "border",
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text       = label,
            color      = if (isFocused) CyanAccent else TextSecondary,
            fontSize   = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
        )
        Spacer(Modifier.height(6.dp))

        BasicTextField(
            value          = value,
            onValueChange  = onValueChange,
            singleLine     = singleLine,
            maxLines       = maxLines,
            keyboardOptions   = keyboardOptions,
            keyboardActions   = keyboardActions,
            visualTransformation = visualTransformation,
            cursorBrush    = SolidColor(CyanAccent),
            textStyle      = TextStyle(color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Normal),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            decorationBox  = { innerField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FieldShape)
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, borderColor, FieldShape)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                ) {
                    if (value.isEmpty()) {
                        Text(hint, color = TextHint, fontSize = 14.sp)
                    }
                    Box(Modifier.fillMaxWidth().padding(end = if (trailingIcon != null) 40.dp else 0.dp)) {
                        innerField()
                    }
                    if (trailingIcon != null) {
                        Box(Modifier.align(Alignment.CenterEnd)) { trailingIcon() }
                    }
                }
            },
        )

        if (isError && errorMessage.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(errorMessage, color = ErrorRed, fontSize = 11.sp)
        }
    }
}
