package com.example.boardingbookingapp.data.model

data class SupportTicket(
    val id: String = "",
    val submittedBy: String = "",
    val submitterName: String = "",
    val submitterRole: UserRole = UserRole.STUDENT,
    val subject: String = "",
    val description: String = "",
    val status: TicketStatus = TicketStatus.OPEN,
    val priority: TicketPriority = TicketPriority.MEDIUM,
    val adminNotes: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)

enum class TicketStatus { OPEN, IN_PROGRESS, RESOLVED, CLOSED }
enum class TicketPriority { LOW, MEDIUM, HIGH, URGENT }
