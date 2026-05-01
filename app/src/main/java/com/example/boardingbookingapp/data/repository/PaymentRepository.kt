package com.example.boardingbookingapp.data.repository

import com.example.boardingbookingapp.data.model.Payment
import com.example.boardingbookingapp.data.model.SplitBill
import com.example.boardingbookingapp.util.Result
import kotlinx.coroutines.flow.Flow

// Sprint 6 — implement with Firestore + Firebase Storage (receipt upload)
interface PaymentRepository {
    fun getPayments(tenantId: String): Flow<List<Payment>>
    fun getPendingPayments(ownerId: String): Flow<List<Payment>>
    suspend fun recordPayment(payment: Payment): Result<Payment>
    suspend fun uploadReceipt(paymentId: String, imageUri: String): Result<String>
    suspend fun verifyReceipt(paymentId: String): Result<Unit>
    fun getSplitBills(listingId: String): Flow<List<SplitBill>>
    suspend fun createSplitBill(bill: SplitBill): Result<SplitBill>
    suspend fun markSplitPaid(billId: String, userId: String, receiptUri: String): Result<Unit>
}
