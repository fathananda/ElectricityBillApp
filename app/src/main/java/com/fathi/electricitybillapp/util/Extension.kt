package com.fathi.electricitybillapp.util

import com.fathi.electricitybillapp.data.bill.Bill
import com.fathi.electricitybillapp.data.bill.BillEntity
import com.fathi.electricitybillapp.data.usage.ElectricUsage
import com.fathi.electricitybillapp.data.usage.ElectricUsageEntity
import com.fathi.electricitybillapp.data.user.User
import com.fathi.electricitybillapp.data.user.UserEntity
import com.fathi.electricitybillapp.data.user.UserRole

fun UserEntity.toUser(): User {
    return User(
        id = id,
        username = username,
        password = password,
        role = UserRole.valueOf(role),
        customerId = customerId,
        name = name,
        email = email,
        phone = phone,
        address = address,
        createdAt = createdAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        password = password,
        role = role.name,
        customerId = customerId,
        name = name,
        email = email,
        phone = phone,
        address = address,
        createdAt = createdAt
    )
}

fun ElectricUsageEntity.toElectricUsage(): ElectricUsage {
    return ElectricUsage(
        id = id,
        customerId = customerId,
        month = month,
        year = year,
        previousReading = previousReading,
        currentReading = currentReading,
        usageKwh = usageKwh,
        ratePerKwh = ratePerKwh,
        totalAmount = totalAmount,
        isPaid = isPaid,
        dueDate = dueDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ElectricUsage.toEntity(): ElectricUsageEntity {
    return ElectricUsageEntity(
        id = id,
        customerId = customerId,
        month = month,
        year = year,
        previousReading = previousReading,
        currentReading = currentReading,
        usageKwh = usageKwh,
        ratePerKwh = ratePerKwh,
        totalAmount = totalAmount,
        isPaid = isPaid,
        dueDate = dueDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun BillEntity.toBill(): Bill {
    return Bill(
        id = id,
        customerId = customerId,
        usageId = usageId,
        month = month,
        year = year,
        usageKwh = usageKwh,
        amount = amount,
        adminFee = adminFee,
        totalAmount = totalAmount,
        isPaid = isPaid,
        paidAt = paidAt,
        dueDate = dueDate,
        createdAt = createdAt
    )
}

fun Bill.toEntity(): BillEntity {
    return BillEntity(
        id = id,
        customerId = customerId,
        usageId = usageId,
        month = month,
        year = year,
        usageKwh = usageKwh,
        amount = amount,
        adminFee = adminFee,
        totalAmount = totalAmount,
        isPaid = isPaid,
        paidAt = paidAt,
        dueDate = dueDate,
        createdAt = createdAt
    )
}
