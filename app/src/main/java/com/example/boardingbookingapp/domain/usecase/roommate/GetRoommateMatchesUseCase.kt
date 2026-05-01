package com.example.boardingbookingapp.domain.usecase.roommate

import com.example.boardingbookingapp.data.model.RoommateProfile
import com.example.boardingbookingapp.data.model.StudyHabit
import com.example.boardingbookingapp.data.model.SleepPattern

// Sprint 4 — pure scoring function, no injection needed
class GetRoommateMatchesUseCase {

    operator fun invoke(
        myProfile: RoommateProfile,
        candidates: List<RoommateProfile>,
    ): List<RoommateProfile> =
        candidates
            .filter { it.isLookingForRoommate && it.userId != myProfile.userId }
            .map { it.copy(matchScore = score(myProfile, it)) }
            .sortedByDescending { it.matchScore }

    private fun score(a: RoommateProfile, b: RoommateProfile): Int {
        var score = 0
        if (a.studyHabit == b.studyHabit) score += 20
        if (a.sleepPattern == b.sleepPattern) score += 20
        if (a.smoking == b.smoking) score += 15
        if (a.drinking == b.drinking) score += 15
        val sharedHobbies = a.hobbies.intersect(b.hobbies.toSet()).size
        score += (sharedHobbies * 5).coerceAtMost(20)
        val budgetOverlap = a.budgetMax >= b.budgetMin && b.budgetMax >= a.budgetMin
        if (budgetOverlap) score += 10
        return score.coerceIn(0, 100)
    }
}
