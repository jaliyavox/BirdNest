package com.example.birdnest.ui.screens.listings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.birdnest.data.model.GenderPolicy
import com.example.birdnest.data.model.RoomType
import com.example.birdnest.ui.components.*
import com.example.birdnest.ui.theme.*

private val STEPS = listOf("Photos", "Details", "Amenities", "Location", "Review")
private val ALL_AMENITIES = listOf("WiFi", "AC", "Fan", "Hot Water", "Parking", "Laundry", "Kitchen", "Security", "Meals", "Garden", "Study Room", "Common Room")

@Composable
fun PostListingScreen(
    onListingPosted: () -> Unit,
    onBack: () -> Unit,
) {
    var step by remember { mutableIntStateOf(0) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedRoomType by remember { mutableStateOf<RoomType?>(null) }
    var selectedGenderPolicy by remember { mutableStateOf<GenderPolicy?>(null) }
    var selectedAmenities by remember { mutableStateOf(setOf<String>()) }
    var address by remember { mutableStateOf("") }
    var photosAdded by remember { mutableIntStateOf(0) }

    ModernBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding(),
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = { if (step > 0) step-- else onBack() },
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ModernTextPrimary)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Post a Listing", color = ModernTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Step ${step + 1} of ${STEPS.size} — ${STEPS[step]}", color = ModernTextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Progress bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                STEPS.forEachIndexed { i, _ ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    i < step  -> ModernPrimary
                                    i == step -> ModernPrimary
                                    else      -> ModernBlueSoft
                                }
                            ),
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
            ) {
                when (step) {
                    0 -> PhotoStep(photosAdded = photosAdded, onAddPhoto = { photosAdded++ })
                    1 -> DetailsStep(
                        title = title, onTitleChange = { title = it },
                        description = description, onDescriptionChange = { description = it },
                        price = price, onPriceChange = { price = it },
                        selectedRoomType = selectedRoomType, onRoomTypeSelect = { selectedRoomType = it },
                        selectedGenderPolicy = selectedGenderPolicy, onGenderSelect = { selectedGenderPolicy = it },
                    )
                    2 -> AmenitiesStep(selected = selectedAmenities, onToggle = { a ->
                        selectedAmenities = if (a in selectedAmenities) selectedAmenities - a else selectedAmenities + a
                    })
                    3 -> LocationStep(address = address, onAddressChange = { address = it })
                    4 -> ReviewStep(
                        title = title, price = price, roomType = selectedRoomType,
                        gender = selectedGenderPolicy, amenities = selectedAmenities,
                        address = address, photos = photosAdded,
                    )
                }
            }

            // Navigation buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(24.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (step > 0) {
                        ModernButton(
                            text = "Previous",
                            onClick = { step-- },
                            modifier = Modifier.weight(1f),
                            containerColor = ModernBlueSoft,
                            contentColor = ModernPrimary
                        )
                    }
                    ModernButton(
                        text    = if (step < STEPS.lastIndex) "Next Step" else "Submit Listing",
                        onClick = { if (step < STEPS.lastIndex) step++ else onListingPosted() },
                        modifier = Modifier.weight(1f),
                        enabled = when (step) {
                            0 -> photosAdded > 0
                            1 -> title.isNotBlank() && price.isNotBlank() && selectedRoomType != null
                            3 -> address.isNotBlank()
                            else -> true
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoStep(photosAdded: Int, onAddPhoto: () -> Unit) {
    Column {
        Text("Property Photos", color = ModernTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("High quality photos help students find your place faster.", color = ModernTextSecondary, fontSize = 15.sp)
        Spacer(Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(maxOf(1, photosAdded + 1).coerceAtMost(4)) { i ->
                val isAdd = i == photosAdded
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isAdd) ModernBlueSoft else ModernPrimary.copy(alpha = 0.1f))
                        .then(if (isAdd) Modifier.clickable(onClick = onAddPhoto) else Modifier),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isAdd) {
                        Icon(Icons.Default.AddPhotoAlternate, null, tint = ModernPrimary, modifier = Modifier.size(32.dp))
                    } else {
                        Text("${i + 1}", color = ModernPrimary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        if (photosAdded > 0) {
            Spacer(Modifier.height(16.dp))
            Text("$photosAdded photos added successfully", color = SuccessGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DetailsStep(
    title: String, onTitleChange: (String) -> Unit,
    description: String, onDescriptionChange: (String) -> Unit,
    price: String, onPriceChange: (String) -> Unit,
    selectedRoomType: RoomType?, onRoomTypeSelect: (RoomType) -> Unit,
    selectedGenderPolicy: GenderPolicy?, onGenderSelect: (GenderPolicy) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text("General Details", color = ModernTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        ModernTextField(value = title, onValueChange = onTitleChange, label = "LISTING TITLE", placeholder = "e.g. Furnished Room Near SLIIT")
        ModernTextField(value = description, onValueChange = onDescriptionChange, label = "DESCRIPTION", placeholder = "Describe your property...", errorMessage = "Min 20 characters")
        ModernTextField(value = price, onValueChange = { if (it.all { c -> c.isDigit() }) onPriceChange(it) }, label = "MONTHLY RENT (Rs.)", placeholder = "e.g. 15000")

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("ROOM TYPE", color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RoomType.entries.forEach { rt ->
                    ModernFilterChip(label = rt.name.replace("_", " "), selected = selectedRoomType == rt) { onRoomTypeSelect(rt) }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("GENDER PREFERENCE", color = ModernTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GenderPolicy.entries.forEach { gp ->
                    ModernFilterChip(label = gp.name, selected = selectedGenderPolicy == gp) { onGenderSelect(gp) }
                }
            }
        }
    }
}

@Composable
private fun AmenitiesStep(selected: Set<String>, onToggle: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text("Amenities", color = ModernTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("What does your property offer?", color = ModernTextSecondary, fontSize = 15.sp)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ALL_AMENITIES.forEach { a ->
                ModernFilterChip(label = a, selected = a in selected) { onToggle(a) }
            }
        }
    }
}

@Composable
private fun LocationStep(address: String, onAddressChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text("Location", color = ModernTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        ModernTextField(value = address, onValueChange = onAddressChange, label = "STREET ADDRESS", placeholder = "e.g. 45, Kaduwela Rd, Malabe")

        ModernCard(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(ModernBlueSoft),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LocationOn, null, tint = ModernPrimary, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("Pin on Map", color = ModernPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ReviewStep(
    title: String, price: String, roomType: RoomType?,
    gender: GenderPolicy?, amenities: Set<String>,
    address: String, photos: Int,
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text("Review & Submit", color = ModernTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Your listing will be verified by our team within 24 hours.", color = ModernTextSecondary, fontSize = 15.sp)

        ModernCard(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ReviewItem("Title", title.ifBlank { "—" })
                ReviewItem("Monthly Rent", if (price.isNotBlank()) "Rs. $price" else "—")
                ReviewItem("Type", roomType?.name?.replace("_", " ") ?: "—")
                ReviewItem("Preference", gender?.name ?: "—")
                ReviewItem("Address", address.ifBlank { "—" })
                ReviewItem("Amenities", if (amenities.isEmpty()) "None" else amenities.joinToString(", "))
            }
        }
    }
}

@Composable
private fun ReviewItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = ModernTextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(value, color = ModernTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.weight(1f).padding(start = 16.dp))
    }
}
