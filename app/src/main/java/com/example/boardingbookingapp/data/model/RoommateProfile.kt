package com.example.boardingbookingapp.data.model

data class RoommateProfile(
    val userId: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val gender: String = "",
    val faculty: String = "",
    val year: Int = 1,
    val hobbies: List<String> = emptyList(),
    val studyHabit: StudyHabit = StudyHabit.FLEXIBLE,
    val sleepPattern: SleepPattern = SleepPattern.FLEXIBLE,
    val smoking: Boolean = false,
    val drinking: Boolean = false,
    val budgetMin: Int = 0,
    val budgetMax: Int = 0,
    val isLookingForRoommate: Boolean = true,
    val bio: String = "",
    val matchScore: Int = 0,
)

enum class StudyHabit { EARLY_MORNING, NIGHT_OWL, FLEXIBLE }
enum class SleepPattern { EARLY_BIRD, NIGHT_OWL, FLEXIBLE }
