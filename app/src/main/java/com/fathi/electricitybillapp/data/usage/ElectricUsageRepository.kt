package com.fathi.electricitybillapp.data.usage

import com.fathi.electricitybillapp.data.bill.Bill
import com.fathi.electricitybillapp.data.bill.BillDao
import com.fathi.electricitybillapp.util.toElectricUsage
import com.fathi.electricitybillapp.util.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectricUsageRepository @Inject constructor(
    private val electricUsageDao: ElectricUsageDao,
    private val billDao: BillDao
) {
    suspend fun getUsageByCustomer(customerId: String): List<ElectricUsage> {
        return electricUsageDao.getUsageByCustomer(customerId).map { it.toElectricUsage() }
    }

    suspend fun getAllUsages(): List<ElectricUsage> {
        return electricUsageDao.getAllUsages().map { it.toElectricUsage() }
    }

    /**
     * Menyimpan data penggunaan listrik dan membuat tagihan otomatis
     *
     * @param usage Data penggunaan listrik
     * @return Long ID penggunaan yang baru dibuat
     *
     * Fungsi:
     * - Menyimpan data penggunaan ke database
     * - Membuat tagihan otomatis berdasarkan penggunaan
     * - Menghitung total tagihan dengan biaya admin
     */

    suspend fun insertUsage(usage: ElectricUsage): Long {
        val usageId = electricUsageDao.insertUsage(usage.toEntity())

        // Auto-generate bill
        val bill = Bill(
            customerId = usage.customerId,
            usageId = usageId.toInt(),
            month = usage.month,
            year = usage.year,
            usageKwh = usage.usageKwh,
            amount = usage.totalAmount,
            adminFee = 2500.0,
            totalAmount = usage.totalAmount + 2500.0,
            dueDate = usage.dueDate
        )
        billDao.insertBill(bill.toEntity())

        return usageId
    }

    suspend fun updateUsage(usage: ElectricUsage) {
        electricUsageDao.updateUsage(usage.toEntity())
    }

    suspend fun deleteUsage(usage: ElectricUsage) {
        electricUsageDao.deleteUsage(usage.toEntity())
    }

    suspend fun getUsageByMonthYear(customerId: String, month: Int, year: Int): ElectricUsage? {
        return electricUsageDao.getUsageByMonthYear(customerId, month, year)?.toElectricUsage()
    }
}