package com.example.birdnest.ui.screens.roommate

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdnest.data.model.SleepPattern
import com.example.birdnest.data.model.StudyHabit
import com.example.birdnest.ui.components.GlassButton
import com.example.birdnest.ui.components.GlassCard
import com.example.birdnest.ui.components.GlassTextField
import com.example.birdnest.ui.components.GradientBackground
import com.example.birdnest.ui.theme.CyanAccent
import com.example.birdnest.ui.theme.TextSecondary
import com.example.birdnest.ui.theme.TextTertiary
import com.example.birdnest.ui.theme.VioletPrimary

private val ALL_HOBBIES = listOf("Gaming", "Reading", "Cooking", "Gym", "Music", "Movies", "Sports", "Coding", "Art", "Travel", "Yoga", "Photography")
private val FACULTIES = listOf("FCIIT", "FOE", "FOM", "FHSS", "FNDT", "FGS")

@Composable
fun RoommateProfileScreen(
    onSaved: () -> Unit,
    onBack: () -> Unit,
) {
    var bio by remember { mutableStateOf("") }
    var selectedFaculty by remember { mutableStateOf("FCIIT") }
    var year by remember { mutableStateOf("2") }
    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }
    var studyHabit by remember { mutableStateOf(StudyHabit.FLEXIBLE) }
    var sleepPattern by remember { mutableStateOf(SleepPattern.FLEXIBLE) }
    var smoking by remember { mutableStateOf(false) }
    var budgetMin by remember { mutableStateOf("") }
    var budgetMax by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                }
                Text("Roommate Profile", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Basic Info", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("FACULTY", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FACULTIES.forEach { f ->
                                    SelectChip(f, selectedFaculty == f) { selectedFaculty = f }
                                }
                            }
                        }

                        GlassTextField(
                            value = year, onValueChange = { if (it.length <= 1 && it.all { c -> c.isDigit() }) year = it },
                            label = "YEAR OF STUDY", hint = "e.g. 2",
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        )

                        GlassTextField(
                            value = bio, onValueChange = { bio = it },
                            label = "BIO", hint = "Tell potential roommates about yourself...",
                            singleLine = false, maxLines = 3,
                        )
                    }
                }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Lifestyle", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                        HabitSelector("STUDY HABIT", StudyHabit.entries.map { it.name.replace("_", " ") }, studyHabit.ordinal) { studyHabit = StudyHabit.entries[it] }
                        HabitSelector("SLEEP PATTERN", SleepPattern.entries.map { it.name.replace("_", " ") }, sleepPattern.ordinal) { sleepPattern = SleepPattern.entries[it] }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Smoking", color = TextSecondary, fontSize = 14.sp)
                            Switch(checked = smoking, onCheckedChange = { smoking = it }, colors = SwitchDefaults.colors(checkedThumbColor = CyanAccent, checkedTrackColor = CyanAccent.copy(0.30f)))
                        }
                    }
                }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Hobbies & Interests", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        val rows = ALL_HOBBIES.chunked(4)
                        rows.forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEach { h ->
                                    SelectChip(h, h in selectedHobbies) { selectedHobbies = if (h in selectedHobbies) selectedHobbies - h else selectedHobbies + h }
                                }
                            }
                        }
                    }
                }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Budget Range (Rs./month)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            GlassTextField(value = budgetMin, onValueChange = { budgetMin = it }, label = "MIN", hint = "8000", modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
                            GlassTextField(value = budgetMax, onValueChange = { budgetMax = it }, label = "MAX", hint = "20000", modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done))
                        }
                    }
                }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Public Profile", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                            Text("Visible to other students", color = TextTertiary, fontSize = 12.sp)
                        }
                        Switch(checked = isPublic, onCheckedChange = { isPublic = it }, colors = SwitchDefaults.colors(checkedThumbColor = CyanAccent, checkedTrackColor = CyanAccent.copy(0.30f)))
                    }
                }

                GlassButton(
                    text    = "Save Profile",
                    onClick = onSaved,
                    enabled = bio.isNotBlank() || selectedHobbies.isNotEmpty(),
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SelectChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .clip(shape)
            .background(if (selected) VioletPrimary.copy(0.60f) else Color.White.copy(0.08f))
            .border(1.dp, if (selected) CyanAccent.copy(0.50f) else Color.White.copy(0.15f), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (selected) Icon(Icons.Default.Check, null, tint = CyanAccent, modifier = Modifier.size(11.dp))
            Text(label, color = if (selected) Color.White else TextSecondary, fontSize = 12.sp)
        }
    }
}

@Composable
private fun HabitSelector(title: String, options: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEachIndexed { i, opt ->
                SelectChip(opt.split(" ").last(), i == selected) { onSelect(i) }
            }
        }
    }
}
