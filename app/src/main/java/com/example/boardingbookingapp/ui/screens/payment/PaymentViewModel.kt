package com.example.birdnest.ui.screens.payment

import androidx.lifecycle.ViewModel
import com.example.birdnest.data.model.Payment
import com.example.birdnest.data.model.SplitBill
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Sprint 6 — inject PaymentRepository via @HiltViewModel
class PaymentViewModel : ViewModel() {

    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments.asStateFlow()

    private val _splitBills = MutableStateFlow<List<SplitBill>>(emptyList())
    val splitBills: StateFlow<List<SplitBill>> = _splitBills.asStateFlow()

    fun loadPayments(tenantId: String) {
        // Sprint 6: collect paymentRepository.getPayments(tenantId)
    }

    fun initiatePayhere(payment: Payment) {
        // Sprint 6: paymentRepository.initiatePayhere(payment)
    }

    fun createSplitBill(bill: SplitBill) {
        // Sprint 6: paymentRepository.createSplitBill(bill)
    }
}
