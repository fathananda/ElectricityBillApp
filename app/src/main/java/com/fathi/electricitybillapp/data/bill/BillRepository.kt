package com.fathi.electricitybillapp.data.bill

import com.fathi.electricitybillapp.util.toBill
import com.fathi.electricitybillapp.util.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillRepository @Inject constructor(
    private val billDao: BillDao
) {
    suspend fun getBillsByCustomer(customerId: String): List<Bill> {
        return billDao.getBillsByCustomer(customerId).map { it.toBill() }
    }

    suspend fun getAllBills(): List<Bill> {
        return billDao.getAllBills().map { it.toBill() }
    }

    /**
     * Memperbarui status tagihan (untuk pembayaran)
     *
     * @param bill Tagihan yang akan diperbarui
     *
     * Fungsi:
     * - Memperbarui status pembayaran tagihan
     * - Mencatat waktu pembayaran
     *
     * Exceptions:
     * - SQLException: Jika terjadi error database
     * - IllegalStateException: Jika tagihan sudah dibayar
     */
    suspend fun updateBill(bill: Bill) {
        billDao.updateBill(bill.toEntity())
    }
}