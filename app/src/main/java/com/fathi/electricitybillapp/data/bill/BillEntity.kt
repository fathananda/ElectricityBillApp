package com.fathi.electricitybillapp.data.bill

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val customerId: String,
    val usageId: Int,
    val month: Int,
    val year: Int,
    val usageKwh: Double,
    val amount: Double,
    val adminFee: Double,
    val totalAmount: Double,
    val isPaid: Boolean,
    val paidAt: Long?,
    val dueDate: Long,
    val createdAt: Long
)