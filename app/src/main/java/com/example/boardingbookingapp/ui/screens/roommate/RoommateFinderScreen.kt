package com.example.boardingbookingapp.ui.screens.roommate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardingbookingapp.data.model.RoommateProfile
import com.example.boardingbookingapp.data.model.StudyHabit
import com.example.boardingbookingapp.data.model.SleepPattern
import com.example.boardingbookingapp.ui.components.*
import com.example.boardingbookingapp.ui.theme.*

@Composable
fun RoommateFinderScreen(
    onOpenProfile: () -> Unit,
    onMessageRoommate: (String) -> Unit,
    onBack: () -> Unit,
) {
    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ModernTextPrimary)
                    }
                    Column {
                        Text("Roommate Finder", color = ModernTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                        Text("${MOCK_PROFILES.size} students searching", color = ModernTextSecondary, fontSize = 13.sp)
                    }
                }
                IconButton(
                    onClick = onOpenProfile,
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(ModernBlueSoft)
                ) {
                    Icon(Icons.Default.Edit, null, tint = ModernPrimary, modifier = Modifier.size(20.dp))
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    ModernCard(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(
                                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(ModernBlueSoft),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(Icons.Default.People, null, tint = ModernPrimary, modifier = Modifier.size(28.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Complete your profile", color = ModernTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Get matched with compatible peers", color = ModernTextSecondary, fontSize = 13.sp)
                            }
                            ModernButton(
                                text = "Setup",
                                onClick = onOpenProfile,
                                modifier = Modifier.width(80.dp).height(40.dp),
                                containerColor = ModernPrimary
                            )
                        }
                    }
                }

                items(MOCK_PROFILES, key = { it.userId }) { profile ->
                    RoommateMatchItem(profile = profile, onMessage = { onMessageRoommate(profile.userId) })
                }
            }
        }
    }
}

@Composable
private fun RoommateMatchItem(profile: RoommateProfile, onMessage: () -> Unit) {
    val matchColor = when {
        profile.matchScore >= 80 -> SuccessGreen
        profile.matchScore >= 60 -> ModernPrimary
        profile.matchScore >= 40 -> WarningAmber
        else                     -> ModernTextTertiary
    }

    ModernCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(verticalAlignment = Alignment.Top) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(ModernBlueSoft),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(profile.displayName.take(1), color = ModernPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(profile.displayName, color = ModernTextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text("${profile.faculty} · Year ${profile.year}", color = ModernTextSecondary, fontSize = 13.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Budget: Rs. ${profile.budgetMin}–${profile.budgetMax}", color = ModernPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                // Match score
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(matchColor.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${profile.matchScore}%", color = matchColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("match", color = matchColor.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(profile.bio, color = ModernTextSecondary, fontSize = 14.sp, maxLines = 2, lineHeight = 20.sp)

            if (profile.hobbies.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    profile.hobbies.take(3).forEach { hobby ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ModernBackground)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                        ) {
                            Text(hobby, color = ModernTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            ModernButton(
                text = "Send Message",
                onClick = onMessage,
                containerColor = ModernBlueSoft,
                contentColor = ModernPrimary
            )
        }
    }
}

private val MOCK_PROFILES = listOf(
    RoommateProfile(userId="r1", displayName="Kasun Perera", gender="Male", faculty="FCIIT", year=2, hobbies=listOf("Gaming","Gym","Music"), studyHabit=StudyHabit.NIGHT_OWL, sleepPattern=SleepPattern.NIGHT_OWL, smoking=false, budgetMin=10000, budgetMax=18000, bio="3rd year SE student. Looking for a quiet place near SLIIT. Non-smoker, clean.", matchScore=92),
    RoommateProfile(userId="r2", displayName="Amaya Jayasinghe", gender="Female", faculty="FCIIT", year=1, hobbies=listOf("Reading","Cooking","Yoga"), studyHabit=StudyHabit.EARLY_MORNING, sleepPattern=SleepPattern.EARLY_BIRD, smoking=false, budgetMin=8000, budgetMax=15000, bio="1st year IT student. Early riser, tidy, loves cooking. Prefer girls only.", matchScore=78),
    RoommateProfile(userId="r3", displayName="Tharaka Bandara", gender="Male", faculty="FOE", year=3, hobbies=listOf("Cricket","Coding","Movies"), studyHabit=StudyHabit.FLEXIBLE, sleepPattern=SleepPattern.FLEXIBLE, smoking=false, budgetMin=12000, budgetMax=20000, bio="Engineering student. Flexible schedule, friendly. Have a bicycle for commute.", matchScore=65),
    RoommateProfile(userId="r4", displayName="Dilini Silva", gender="Female", faculty="FCIIT", year=2, hobbies=listOf("Art","Music","Travel"), studyHabit=StudyHabit.NIGHT_OWL, sleepPattern=SleepPattern.FLEXIBLE, smoking=false, budgetMin=9000, budgetMax=16000, bio="Design student. Creative, night owl, very social. Looking for mixed or girls house.", matchScore=55),
    RoommateProfile(userId="r5", displayName="Nuwan Rathnayake", gender="Male", faculty="FOM", year=4, hobbies=listOf("Football","Finance","Reading"), studyHabit=StudyHabit.EARLY_MORNING, sleepPattern=SleepPattern.EARLY_BIRD, smoking=false, budgetMin=15000, budgetMax=25000, bio="Final year management student. Serious about studies, tidy, early sleeper.", matchScore=47),
)
