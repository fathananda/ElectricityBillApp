package com.fathi.electricitybillapp.data.bill

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fathi.electricitybillapp.data.bill.BillEntity

@Dao
interface BillDao {
    @Query("SELECT * FROM bills WHERE customerId = :customerId ORDER BY year DESC, month DESC")
    suspend fun getBillsByCustomer(customerId: String): List<BillEntity>

    @Query("SELECT * FROM bills WHERE id = :id")
    suspend fun getBillById(id: Int): BillEntity?

    @Query("SELECT * FROM bills ORDER BY year DESC, month DESC")
    suspend fun getAllBills(): List<BillEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertBill(bill: BillEntity): Long

    @Update
    suspend fun updateBill(bill: BillEntity)

    @Delete
    suspend fun deleteBill(bill: BillEntity)
}