package com.fathi.electricitybillapp.viewmodel.usage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fathi.electricitybillapp.data.usage.ElectricUsage
import com.fathi.electricitybillapp.data.usage.ElectricUsageRepository
import com.fathi.electricitybillapp.viewmodel.usage.ElectricUsageUiState
import com.fathi.electricitybillapp.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectricUsageViewModel @Inject constructor(
    private val electricUsageRepository: ElectricUsageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElectricUsageUiState())
    val uiState = _uiState.asStateFlow()

    fun loadUsages(customerId: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val usages = if (customerId != null) {
                    electricUsageRepository.getUsageByCustomer(customerId)
                } else {
                    electricUsageRepository.getAllUsages()
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    usages = usages
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Gagal memuat data: ${e.message}"
                )
            }
        }
    }

    fun saveUsage(usage: ElectricUsage) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Check if usage already exists for this month/year
                val existing = electricUsageRepository.getUsageByMonthYear(
                    usage.customerId, usage.month, usage.year
                )

                if (existing != null && existing.id != usage.id) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Data penggunaan untuk bulan ini sudah ada"
                    )
                    return@launch
                }

                val calculatedUsage = usage.copy(
                    usageKwh = usage.currentReading - usage.previousReading,
                    totalAmount = (usage.currentReading - usage.previousReading) * usage.ratePerKwh
                )

                if (usage.id == 0) {
                    electricUsageRepository.insertUsage(calculatedUsage)
                } else {
                    electricUsageRepository.updateUsage(calculatedUsage)
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    saveSuccess = true
                )

                // Reload data
                loadUsages()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Gagal menyimpan data: ${e.message}"
                )
            }
        }
    }

    fun deleteUsage(usage: ElectricUsage) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                electricUsageRepository.deleteUsage(usage)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    deleteSuccess = true
                )
                loadUsages()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Gagal menghapus data: ${e.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            saveSuccess = false,
            deleteSuccess = false
        )
    }
}