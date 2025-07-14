package com.fathi.electricitybillapp.viewmodel.bill

import com.fathi.electricitybillapp.data.bill.Bill

data class BillUiState(
    val isLoading: Boolean = false,
    val bills: List<Bill> = emptyList(),
    val paymentSuccess: Boolean = false,
    val error: String? = null
)