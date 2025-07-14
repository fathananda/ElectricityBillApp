package com.fathi.electricitybillapp.viewmodel.bill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fathi.electricitybillapp.data.bill.Bill
import com.fathi.electricitybillapp.data.bill.BillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(
    private val billRepository: BillRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BillUiState())
    val uiState = _uiState.asStateFlow()

    fun loadBills(customerId: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val bills = if (customerId != null) {
                    billRepository.getBillsByCustomer(customerId)
                } else {
                    billRepository.getAllBills()
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    bills = bills
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Gagal memuat tagihan: ${e.message}"
                )
            }
        }
    }

    fun payBill(bill: Bill) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val paidBill = bill.copy(
                    isPaid = true,
                    paidAt = System.currentTimeMillis()
                )
                billRepository.updateBill(paidBill)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    paymentSuccess = true
                )
                loadBills()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Gagal melakukan pembayaran: ${e.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            paymentSuccess = false
        )
    }
}