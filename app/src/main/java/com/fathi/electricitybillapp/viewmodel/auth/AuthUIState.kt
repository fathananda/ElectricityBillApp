package com.fathi.electricitybillapp.viewmodel.auth

data class AuthUiState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null
)
