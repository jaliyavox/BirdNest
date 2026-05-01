package com.example.boardingbookingapp.data.model

data class Payment(
    val id: String = "",
    val tenantId: String = "",
    val ownerId: String = "",
    val listingId: String = "",
    val amount: Int = 0,
    val dueDate: Long = 0L,
    val paidDate: Long? = null,
    val status: PaymentStatus = PaymentStatus.PENDING,
    val type: PaymentType = PaymentType.RENT,
    val receiptUrl: String = "",        // Firebase Storage URL of uploaded receipt image
    val receiptVerified: Boolean = false, // set to true by owner after reviewing receipt
    val note: String = "",
)

data class SplitBill(
    val id: String = "",
    val listingId: String = "",
    val title: String = "",
    val totalAmount: Int = 0,
    val participants: List<SplitParticipant> = emptyList(),
    val createdAt: Long = 0L,
)

data class SplitParticipant(
    val userId: String = "",
    val email: String = "",
    val share: Int = 0,
    val paid: Boolean = false,
)

enum class PaymentStatus { PENDING, PAID, OVERDUE }
enum class PaymentType { RENT, DEPOSIT, UTILITY }
