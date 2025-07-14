package com.fathi.electricitybillapp.data.user

data class User(
    val id: Int = 0,
    val username: String,
    val password: String,
    val role: UserRole,
    val customerId: String? = null,
    val name: String,
    val email: String,
    val phone: String,
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserRole {
    ADMIN, CUSTOMER
}
