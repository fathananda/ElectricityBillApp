package com.fathi.electricitybillapp.data.usage

data class ElectricUsage(
    val id: Int = 0,
    val customerId: String,
    val month: Int,
    val year: Int,
    val previousReading: Double,
    val currentReading: Double,
    val usageKwh: Double,
    val ratePerKwh: Double = 1467.28,
    val totalAmount: Double,
    val isPaid: Boolean = false,
    val dueDate: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)