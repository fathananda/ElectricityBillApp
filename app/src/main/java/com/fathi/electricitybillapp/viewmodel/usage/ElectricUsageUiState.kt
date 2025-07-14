package com.fathi.electricitybillapp.viewmodel.usage

import com.fathi.electricitybillapp.data.usage.ElectricUsage

data class ElectricUsageUiState(
    val isLoading: Boolean = false,
    val usages: List<ElectricUsage> = emptyList(),
    val saveSuccess: Boolean = false,
    val deleteSuccess: Boolean = false,
    val error: String? = null
)