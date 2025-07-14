package com.fathi.electricitybillapp.data.bill

data class Bill(
    val id: Int = 0,
    val customerId: String,
    val usageId: Int,
    val month: Int,
    val year: Int,
    val usageKwh: Double,
    val amount: Double,
    val adminFee: Double = 2500.0,
    val totalAmount: Double,
    val isPaid: Boolean = false,
    val paidAt: Long? = null,
    val dueDate: Long,
    val createdAt: Long = System.currentTimeMillis()
)