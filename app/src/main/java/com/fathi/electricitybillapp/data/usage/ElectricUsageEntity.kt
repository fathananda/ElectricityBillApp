package com.fathi.electricitybillapp.data.usage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "electric_usage")
data class ElectricUsageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val customerId: String,
    val month: Int,
    val year: Int,
    val previousReading: Double,
    val currentReading: Double,
    val usageKwh: Double,
    val ratePerKwh: Double,
    val totalAmount: Double,
    val isPaid: Boolean,
    val dueDate: Long,
    val createdAt: Long,
    val updatedAt: Long
)